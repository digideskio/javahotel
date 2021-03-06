from cutil import printVar
from cutil import getTypeUpList
from cutil import setJMapList
from cutil import createEnum
from cutil import enumDictAction
import cutil

def setCopyF(var,f,val) :
  cutil.setCopy(var,f)
  var[f] = val

def dialogaction(action,var) :
  printVar("packenum",action,var)
    
  if action == "before" :
     var['combof'] = "3"
     var['JCOPY_combof'] = True
     return  
     
  if action == "testcombo" :
    return   
    
  if action == "signalchange" and var["changefield"] == "combof" :
    s = var["combof"]
    setCopyF(var,"outcombof",s)
    return  

  if action == "signalchange" and var["changefield"] == "combome" :
    s = var["combome"]
    setCopyF(var,"outcombom",s)
    return  
    
  seq = []
  for i in range(100) :
        rec = {}
        rec['id'] = str(i)
        rec['name'] = 'name' + str(i)
        seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  

  
def helperaction(action,var) :

  printVar("helperaction",action,var)
    
  seq = []
  for i in range(100) :
        rec = {}
        rec['id'] = str(i)
        rec['name'] = 'helper ' + str(i)
        seq.append(rec)
        
  map = {}
  map[action] = seq
  var["JLIST_MAP"] = map  
    
    
def uptextaction(action,var) :
  printVar("uptextaction",action,var)
  seq = []
  for i in getTypeUpList() :
        rec = {'id' : i, "name" : i }
        seq.append(rec)
 
  setJMapList(var,action,seq)    
  
from com.jythonui.server.holder import Holder  

def langaction(action,var,what) :
    enumDictAction(action,var,what)
#  printVar("coutries action",action,var)
#  iC = getDict(what)
#  seq = createEnum(iC,lambda c : c.getKey(),lambda c : c.getName(), False)
#  print iC
#  for c in iC :
#    rec = {'id' : c.getKey(), 'name' : c.getName() }
#    seq.append(rec)
#  setJMapList(var,action,seq)  