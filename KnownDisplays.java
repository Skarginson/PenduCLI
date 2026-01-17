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

    /**
     * Ajoute un display s'il n'est pas déjà enregistré.
     * @return true si ajouté, false si déjà existant
     */
    public boolean addDisplay(String ip, int port) {
        // Vérifier si ce display existe déjà
        for (DisplayInfo display : displays) {
            if (display.ip.equals(ip) && display.port == port) {
                return false; // Déjà enregistré
            }
        }
        displays.add(new DisplayInfo(ip, port));
        return true;
    }

    public List<DisplayInfo> getDisplays() {
        return displays;
    }

    public boolean isKnownIp(String ip) {
        for (DisplayInfo display : displays) {
            if (display.ip.equals(ip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retire un display de la liste.
     * @return true si retiré, false si non trouvé
     */
    public boolean removeDisplay(String ip, int port) {
        return displays.removeIf(d -> d.ip.equals(ip) && d.port == port);
    }
}
