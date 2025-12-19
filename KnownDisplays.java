import java.util.*;

public class KnownDisplays {

    public static class DisplayInfo {
        public final String ip;
        public final int port;

        public DisplayInfo(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }
    }

    private final List<DisplayInfo> displays;

    public KnownDisplays() {
        this.displays = new ArrayList<>();
    }

    public void addDisplay(String ip, int port) {
        displays.add(new DisplayInfo(ip, port));
    }

    public List<DisplayInfo> getDisplays() {
        return displays;
    }
}
