package core.ingest;

import java.util.HashMap;
import java.util.Map;

public class FlowRecord {

    private int duration;
    private String protocol;
    private String service;
    private String flag;
    private int srcBytes;
    private int dstBytes;
    private String label;

    
    private Map<String, String> values = new HashMap<>();

    public FlowRecord(int duration, String protocol, String service,
                      String flag, int srcBytes, int dstBytes, String label) {

        this.duration = duration;
        this.protocol = protocol;
        this.service = service;
        this.flag = flag;
        this.srcBytes = srcBytes;
        this.dstBytes = dstBytes;
        this.label = label;

  
        values.put("duration", String.valueOf(duration));
        values.put("protocol", protocol);
        values.put("service", service);
        values.put("flag", flag);
        values.put("src_bytes", String.valueOf(srcBytes));
        values.put("dst_bytes", String.valueOf(dstBytes));
        values.put("label", label);
    }

    
    public int getDuration() { return duration; }
    public String getProtocol() { return protocol; }
    public String getService() { return service; }
    public String getFlag() { return flag; }
    public int getSrcBytes() { return srcBytes; }
    public int getDstBytes() { return dstBytes; }
    public String getLabel() { return label; }


    public String get(String columnName) {
        return values.get(columnName);
    }
}
