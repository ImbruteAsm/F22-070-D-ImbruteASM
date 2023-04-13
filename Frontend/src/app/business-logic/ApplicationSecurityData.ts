let data = {
    "applicationSecurityId" : 3,
    "cookies" : {
      "cookiesId" : 3,
      "httpOnly" : null,
      "secure" : null
    },
    "general" : {
      "generalId" : 3,
      "enforceHttps" : null,
      "csp" : null,
      "hsts" : null,
      "unencryptedPwd" : null,
      "xframeOptions" : null,
      "xcontentTypeOptions" : null,
      "corspolicy" : null
    },
    "highSeverityVul" : {
      "highSeverityVulId" : 3,
      "xss" : null,
      "csrf" : null,
      "sqlI" : null
    },
    "riskNumber" : 0.0
}
export class ApplicationSecurityData{
    public httpOnly:any;
    public secure:any;
    public enforcedHttps:any;
    public csp:any;
    public hsts:any;
    public unencryptedPwd:any;
    public xframeOptions:any;
    public xcontentTypeOptions:any;
    public corspolicy:any;

    public xss:any;
    public csrf:any;
    public sqlI:any;

    public riskNumber:any;

    public parseJson(json:any):any{
        if(json==null){return null;}
        if(json['cookies']){
            let cookies=json['cookies']
            this.httpOnly=cookies['httpOnly']
            this.secure=cookies['secure']
        }
        if(json['general']){
            let general=json['general']
            this.enforcedHttps=general['enforcedHttps']
            this.csp=general['csp']
            this.hsts=general['hsts']
            this.unencryptedPwd=general['unencryptedPwd']
            this.xframeOptions=general['xframeOptions']
            this.xcontentTypeOptions=general['xcontentTypeOptions']
            this.corspolicy=general['corspolicy']
        }
        if(json['highSeverityVul']){
            let hsv=json['highSeverityVul']
            this.xss=hsv['xss']
            this.csrf=hsv['csrf']
            this.sqlI=hsv['sqlI']
        }
        this.riskNumber=json['riskNumber']
    }

}