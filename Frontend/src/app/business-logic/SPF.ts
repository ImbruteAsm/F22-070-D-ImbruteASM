
export class SPF {
    public isSpfRecordMissing:boolean = true;
    public isSpfRecordMalformed:boolean = true;
    public doesSpfRecordContainsWildcard:boolean = false;
    public doesspfRecordContainsaSoftfail:boolean = false;
    public target:String | undefined;
}
