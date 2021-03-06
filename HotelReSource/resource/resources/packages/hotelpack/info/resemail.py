import cutil,con,pdfutil,xmlutil,cmail,cmailaction

from util import util,rpdf,rutil,hmail,diallaunch

M = util.MESS()
ALIST=cmailaction.ALIST

def _getCust(var,ma) :
    cust = None
    rese = ma["resename"]
    if ma.has_key("custname") : cust = ma["custname"]
    rform = util.RESFORM(var).findElem(rese)
    assert rform != None
    if cust == None : cust = rform.getCustomerName()
    C = util.CUSTOMERLIST(var).findElem(cust)
    assert C != None
    return (C,rform)

def dialogaction(action,var) :
  cutil.printVar("rese mail",action,var)
  
  if action == "before" :
    xml = var["JUPDIALOG_START"]
    (ma,alist) = xmlutil.toMap(xml)
    rese = ma["resename"]
    mtype = ma["mailtype"]
    (C,rform) = _getCust(var,ma)
    var["to"] = C.getAttr("email")
    var["from"] = cmail.MAILFROM(var).getFrom()
    var["xml"] = xml
    (arrival,departure,roomname,rate,non) = rutil.getReseDate(var,rform)
    xml = rpdf.buildXMLReservation(var,rese)
    if mtype == 0 :
      var["subject"] = M("confirmationmailsubject").format(con.toS(arrival),con.toS(departure))
      var["content"] = pdfutil.xsltHtmlS("xslt/resconfirmation.xslt",xml)
    elif mtype == 1: 
      var["subject"] = M("sendpdfmailsubject").format(con.toS(arrival),con.toS(departure))
      var["content"] = pdfutil.xsltHtmlS("xslt/sendinvoicepdf.xslt",xml)
    else : assert False
    cutil.setCopy(var,["subject","to","from","content","xml"])
    seq = []
    if alist != None :
      for l in alist : 
	 (realm,key,filename) = cutil.splitsubmitres(l["blobid"])
	 seq.append({"filename" : filename, "realm" : realm, "key" : key })
    cutil.setJMapList(var,ALIST,seq)
    return
    
  if action == "sendmail" and var["JYESANSWER"] :
    H = hmail.HotelMail(var)
    xml = var["xml"]
    (ma,templ) = xmlutil.toMap(xml)
    rese = ma["resename"]
    (C,rform) = _getCust(var,ma)
    custname = C.getName()
    mtype = ma["mailtype"]
    to = var["to"]
    fromc = var["from"]
    cmail.MAILFROM(var).saveFrom(fromc)
    content = var["content"]
    subject = var["subject"]
    C.setAttr("email",to)
    util.CUSTOMERLIST(var).changeElem(C)
    attachL = None
    li = var["JLIST_MAP"][ALIST]
    attachL = None
    for l in li : attachL = cmail.createAttachList(attachL,l["realm"],l["key"],l["filename"])	 
    hh = H.sendMail(mtype,rese,custname,subject,content,to,fromc,attachL)
    ressend = H.getCMail(hh.getName()).getSendResult()
    if ressend == None : 
       var["JOK_MESSAGE"] = "@okmailsent"
       var["JCLOSE_DIALOG"] = True
    else :
       var["JERROR_MESSAGE"] = ressend
       var["JMESSAGE_TITLE"] = "@errormailsent"
    cutil.JOURNAL(var).addJournalElem(util.JOURNAL_SENDCONFMAIL,None,hh.getName(),rese)
       
  if action == "sendmail" : return

  cmailaction.mailattachaction(action,var)      
    