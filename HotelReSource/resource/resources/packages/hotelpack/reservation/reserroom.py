import datetime

from util import rutil
from util import util
from util import rbefore
import cutil
import con
import xmlutil

M = util.MESS()

CUST=rbefore.RCUST
RESLIST=rbefore.RLIST

RE=rutil.RELINE(None,"datecol","name","roomservice","roompricelist","serviceperperson","resnop","respriceperson","resnochildren","respricechildren","resnoextrabeds","respriceextrabeds","respriceperroom",None)
            
# --------------------          
def _newRese(var) :
  return rutil.getReseName(var) == None

def _createResData(var,new):
  service = var["roomservice"]
  if cutil.emptyS(service) : return None
  pricelist = var["roompricelist"]
  if cutil.emptyS(pricelist) : return None
  roomname = var["name"]
  date = var["datecol"]
  resdays = var["resdays"]
  dl = datetime.timedelta(1)
  dt = date
  sum = util.SUMBDECIMAL()
  if new :
    list = []
  else :
    list = var["JLIST_MAP"][RESLIST]
    sum.add(var["JFOOTER_reslist_rlist_pricetotal"])
  resnop = var["resnop"]
  perperson = var["serviceperperson"]
  priceroom = var["respriceperroom"]
  priceperson = var["respriceperson"]
  pricechildren = None
  priceextra = None
  resnoc = var["resnochildren"]
  if resnoc : pricechildren = var["respricechildren"]
  resextra = var["resnoextrabeds"]
  if resextra : priceextra = var["respriceextrabeds"]
    
  price = rutil.calculatePrice(perperson,resnop,resnoc,resextra,priceperson,pricechildren,priceextra,priceroom)
  
  query = cutil.createArrayList()
  RES = util.RESOP(var)
  qelem = rutil.createResQueryElem(roomname,date,con.incDays(date,resdays))
  query.add(qelem)
  rList = RES.queryReservation(query)
  allavail = True
  
  resename = rutil.getReseName(var)

  for i in range(resdays) :
#      price = pPrice.getPrice()
      avail = True
      for re in rList :
          rdata = re.getResDate()
          if con.eqDate(dt,rdata) : 
            if resename == None or resename != re.getResId() : 
              avail = allavail = False
      
      map = { "avail" : avail, "resroomname" : roomname, "resday" : dt, "rlist_pricetotal" : price, "rline_nop" : resnop,"rlist_priceperson" : priceperson,
              "rlist_noc" : resnoc, "rlist_pricechildren" : pricechildren, "rlist_noe" : resextra, "rlist_priceextra" : priceextra,              
              "rlist_serviceperperson" : perperson, "rlist_roomservice" : service, "rlist_roompricelist" : pricelist}
      list.append(map)
      dt = dt + dl
      sum.add(price)
    
  return [list,sum.sum,allavail]    

def _getPriceList(var) :
  pricelist = var["roompricelist"]
  serv = var["roomservice"]
  return rutil.getPriceList(var,pricelist,serv)

def _setAfterServiceName(var) :
  S = util.SERVICES(var)
  serv = S.findElem(var["roomservice"])
  if serv == None : return
  var["serviceperperson"] = serv.isPerperson()
  cutil.setCopy(var,"serviceperperson")  

def _setAfterPriceList(var) :
  (price,pricechild,priceextra) = _getPriceList(var)
  var["respriceperson"] = price
  var["respricechildren"] = pricechild
  var["respriceextrabeds"] = priceextra    
  var["respriceperroom"] = price
  cutil.setCopy(var,["respriceperson","respricechildren","respriceextrabeds","respriceperroom"])

def _setAfterPerPerson(var) :  
  cutil.enableField(var,["respriceperson","respricechildren","respriceextrabeds"],var["serviceperperson"])
  cutil.enableField(var,"respriceperroom",not var["serviceperperson"])

def _createListOfDays(var,new): 
  rData = _createResData(var,new)
  if rData == None : return None
  cutil.setJMapList(var,RESLIST,rData[0])
  cutil.setFooter(var,RESLIST,"rlist_pricetotal",rData[1])
  return rData[2]
 
def _checkRese(var,new=True):
  if not RE.validate(var) : return False    
  if _createListOfDays(var,new) : return True
  rutil.setAlreadyReserved(var)
  return False
  
def _checkAvailibity(var) :
  list = var["JLIST_MAP"][RESLIST]  
  res =  rutil.checkReseAvailibity(var,list,"avail","resday","resroomname")
  if res == None : return True
  return False
  
class MAKERESE(util.HOTELTRANSACTION) :
  
   def __init__(self,var) :
     util.HOTELTRANSACTION.__init__(self,1,var)
     
   def run(self,var) :     
      if not _checkAvailibity(var) : return
      # customer firstly
      cust = util.customerFromVar(var,CUST)
      R = util.CUSTOMERLIST(var)
      name = var["cust_name"]
      if not cutil.emptyS(name) :
          cust.setName(name)
          R.changeElem(cust)
      else :
          cust.setGensymbol(True);
          name = R.addElem(cust).getName()
      util.saveDefaCustomer(var,CUST)               
      # --- customer added
      
      resename = rutil.getReseName(var) 
      reservation = util.newResForm(var)
      if resename : reservation.setName(resename)
      else : reservation.setGensymbol(True);
      reservation.setCustomerName(name)
      service = var["roomservice"]
      nop = var["nop"]
      reselist = reservation.getResDetail()
      rlist = var["JLIST_MAP"][RESLIST]
      for re in rlist :
        
          (listprice,listpricechild,listpriceextra) = rutil.getPriceList(var,re["rlist_roompricelist"],re["rlist_roomservice"])

          r = util.newResAddPayment()
          r.setRoomName(re["resroomname"])
          r.setService(re["rlist_roomservice"])
          r.setResDate(con.toDate(re["resday"]))
          r.setPerperson(re["rlist_serviceperperson"])
          r.setPriceListName(re["rlist_roompricelist"])
          
          r.setNoP(re["rline_nop"])
          r.setPrice(con.toB(re["rlist_priceperson"]))
          r.setPriceList(listprice)
          
          util.setIntField(re,"rlist_noc",lambda v : r.setNoChildren(v))
          r.setPriceChildren(con.toB(re["rlist_pricechildren"]))
          r.setPriceListChildren(listpricechild)
          
          util.setIntField(re,"rlist_noe",lambda v : r.setNoExtraBeds(v))
          r.setPriceExtraBeds(con.toB(re["rlist_priceextra"]))
          r.setPriceListExtraBeds(listpriceextra)
          
          r.setPriceTotal(con.toB(re["rlist_pricetotal"]))
          
          reselist.add(r)
          
      RFORM = util.RESFORM(var)
      if resename : RFORM.changeElem(reservation)
      else : RFORM.addElem(reservation)
      var["JCLOSE_DIALOG"] = True
      var["JREFRESH_DATELINE_reservation"] = ""
        
def _checkCurrentRese(var) :
  list = var["JLIST_MAP"][RESLIST]
  if len(list) == 0:
    rutil.setAlreadyReservedNotSelected(var)  
    return False
  for elem in list :
    avail = elem["avail"]
    if not avail :
     rutil.setAlreadyReserved(var)
     return False
  return True          
    
def reseraction(action,var):
    cutil.printVar("reseraction",action,var)
    
    if action == "cancelreserv" and var["JYESANSWER"] :
     util.RESOP(var).changeStatusToCancel(rutil.getReseName(var))
     var["JCLOSE_DIALOG"] = True
     var["JREFRESH_DATELINE_reservation"] = ""
     
    if action == "aftercheckin" and var["JUPDIALOG_BUTTON"] == "makecheckin" :
       util.RESOP(var).changeStatusToStay(rutil.getReseName(var))
       var["JREFRESH_DATELINE_reservation"] = ""
       var["JCLOSE_DIALOG"] = True        
    
    if action == "signalchange" :
        if var["changefield"] == "serviceperperson" :
          _setAfterPerPerson(var)
          
        if var["changefield"] == "roomservice" : 
            _setAfterServiceName(var)
            _setAfterPriceList(var)
            if not var["changeafterfocus"] and _newRese(var) : 
               _setAfterPerPerson(var)
               _createListOfDays(var,True)
        if var["changefield"] == "roompricelist" : 
            _setAfterPriceList(var)
            if not var["changeafterfocus"] and _newRese(var): 
              _setAfterServiceName(var)
              _setAfterPerPerson(var)
              _createListOfDays(var,True)
    
    if action=="before" :
        rbefore.setvarBefore(var)
        if not _newRese(var) :          
          cutil.hideButton(var,["cancelres","checkin"],False)
          util.enableCust(var,CUST,False)
        else :  cutil.hideButton(var,"detailreservation",True) 
        
    if action == "acceptdetails" and (var["JUPDIALOG_BUTTON"] == "accept" or var["JUPDIALOG_BUTTON"] == "acceptask"):
        xml = var["JUPDIALOG_RES"]
        util.xmlToVar(var,xml,util.getCustFieldIdAll(),CUST)
        cutil.setCopy(var,util.getCustFieldIdAll(),None,CUST)
        if not _newRese(var) :          
          name = var[CUST+"name"]
          resename = rutil.getReseName(var)          
          util.RESFORM(var).changeCustName(resename,name)
        
    if action=="custdetails" :
        var["JAFTERDIALOG_ACTION"] = "acceptdetails" 
        if _newRese(var) : util.customerDetailsActive(var,CUST)
        else : util.showCustomerDetailstoActive(var,var[CUST+"name"])
            
    if action == "checkaval" :
        _checkRese(var)

    if action == "askforreservation" :

      if not _checkCurrentRese(var) : return
      var["JYESNO_MESSAGE"] = M("MAKERESERVATIONASK")
      var["JMESSAGE_TITLE"] = ""  
      var["JAFTERDIALOG_ACTION"] = "makereservation"

    if action == "makereservation" and var["JYESANSWER"] :
      TRAN = MAKERESE(var)
      TRAN.doTrans()
      
    if action == "morereservation" :
        l = var["JLIST_MAP"][RESLIST]
        xml = xmlutil.toXML({},l)
        var["JUPDIALOG_START"] = xml
        var["JUP_DIALOG"]="hotel/reservation/searchrooms.xml" 
        var["JAFTERDIALOG_ACTION"] = "morereservationaccept" 
      
    if action == "morereservationaccept" and var["JUPDIALOG_BUTTON"] == "toresrese" :
        arese =  var["resename"]
        var["JUPDIALOG_START"] = var["JUPDIALOG_RES"]        
        rbefore.setvarBefore(var)
        # restore reservation name
        var["resename"] = arese
        _checkRese(var,False)
        
    if action == "detailreservationaccept" :
        xml = var["JUPDIALOG_RES"]
#         xmlutil.xmlToVar(var,xml,RESLIST)
        (rmap,li) = xmlutil.toMapFiltrDialL(xml,var["J_DIALOGNAME"],RESLIST)
        cutil.setJMapList(var,RESLIST,li)

    if action == "detailreservation" :
        l = var["JLIST_MAP"][RESLIST]
        xml = xmlutil.toXML({},l)
        var["JUPDIALOG_START"] = xml
        var["JUP_DIALOG"]="hotel/reservation/modifreservation.xml" 
        var["JAFTERDIALOG_ACTION"] = "detailreservationaccept" 
