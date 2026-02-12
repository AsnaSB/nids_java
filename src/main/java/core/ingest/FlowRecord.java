package core.ingest;

public class FlowRecord {

    private int duration;
    private String protocol;
    private String service;
    private String flag;
    private int srcBytes;
    private int dstBytes;
    private String label;

    public FlowRecord(int duration, String protocol, String service,
                      String flag, int srcBytes, int dstBytes, String label) {
        this.duration = duration;
        this.protocol = protocol;
        this.service = service;
        this.flag = flag;
        this.srcBytes = srcBytes;
        this.dstBytes = dstBytes;
        this.label = label;
    }

    public int getDuration() { return duration; }
    public String getProtocol() { return protocol; }
    public String getService() { return service; }
    public String getFlag() { return flag; }
    public int getSrcBytes() { return srcBytes; }
    public int getDstBytes() { return dstBytes; }
    public String getLabel() { return label; }
}
