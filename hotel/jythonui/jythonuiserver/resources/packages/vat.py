import cutil,con

def calculateVatBrutto(brutto,level):
    """ Calculate vat tax from brutto price
    Args:
        brutto : Price brutto (including tax)
        level : tax level in percent (example: 22, 17) 
    
    Returns:
        (netto, vat) : price netto and vat tax (netto+vat=brutto)
    
    """
    if level == None : return (brutto,None)
    v = level/( 100  + level)
    vat = con.mulDecimal(brutto,v)
    return (con. minusDecimal(brutto,vat),vat) 

class CalcVat :
    """ Calculate vat for a list of items
    
    Aggregates and calculates vat for a list of items. Add items one by one and finally calculate
    aggregated vat for every vat level
    
    """
    def __init__(self):
        """ Constructor
        Prepares list of empty vat level using standard vat dictionary (cutil.getDict("vat"))
        """
        self.lvat = {}
        for v in cutil.getDict("vat") :
            l = v.getAttr("level")
            if l == "" : self.lvat[v.getName()] = None
            else : self.lvat[v.getName()] = float(l)
        self.bvat = {}
        self.met = 1
        
    def calculateVatValue(self,brutto,vats):
        level = self.lvat[vats]
        (netto,vatv) =  calculateVatBrutto(brutto,level)
        return (netto,vatv,level)
       
    def addVatLine(self,brutto,vat):
        """ Add next item 
        
        Args:
            brutto : Brutto prive
            vat : vat name, string (not level, number) from cutil.getDict("vat") dictionary
        
        """
        abrutto = 0.0
        if self.bvat.has_key(vat) : abrutto = self.bvat[vat]
        self.bvat[vat] = con.addDecimal(abrutto,brutto)
        self.met = 1
        
    def addVatLineC(self,brutto,netto,vat,vats):
        if self.bvat.has_key(vats) : (abrutto,anetto,avat) = self.bvat[vats]
        else : (abrutto,anetto,avat) = (0,0,0)
        abrutto = con.addDecimal(abrutto,brutto)
        anetto = con.addDecimal(anetto,netto)
        avat = con.addDecimal(avat,vat)
        self.bvat[vats] = (abrutto,anetto,avat)
        self.met = 0
        
    def calculateVat(self):
        """ Final calculation of vat
        
        Returns:
          List of calculated vat level. Every element in the list consists pair (netto, vat tax,brutto,vat level)
        """
        lvat = []
        for v in self.bvat : 
            level = self.lvat[v]
            if self.met == 1 :
                (netto,vat) = calculateVatBrutto(self.bvat[v],level)
                brutto = self.bvat[v]
            else : (brutto,netto,vat) = self.bvat[v]
            lvat.append((netto,vat,brutto,level,v))
        return lvat