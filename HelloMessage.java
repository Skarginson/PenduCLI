import java.net.*;

/**
 * Représente et parse un message HELLO du protocole :
 *
 * HELLO
 * <ip>
 * <port>
 */

public class HelloMessage {
	private final String ip;
	private final int port;

	public HelloMessage(String ip, int port) {
		validate(ip, port);
		this.ip = ip;
		this.port = port;
	}

	private static void validate(String ip, int port) {
		if (ip == null || ip.isEmpty()) {
			throw new IllegalArgumentException("IP vide");
		}
		if (port < 1 || port > 65535) {
			throw new IllegalArgumentException("Port invalide: " + port);
		}
		if (port == 2025) {
			throw new IllegalArgumentException("Le port ne doit pas être 2025");
		}
		try {
			InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("IP invalide: " + ip, e);
		}
	}

	/**
	 * Parse un message HELLO (trois lignes) et retourne un HelloMessage.
	 * Lance IllegalArgumentException si le message est mal formé.
	 */

	public static HelloMessage parse(String message) {
		if (message == null) {
			throw new IllegalArgumentException("Message nul");
		}

		String[] lines = message.trim().split("\\R");
		if (lines.length < 3) {
			throw new IllegalArgumentException("Message HELLO incomplet");
		}

		String head = lines[0].trim();
		if (!"HELLO".equals(head)) {
			throw new IllegalArgumentException("En-tête attendu: HELLO, trouvé: " + head);
		}
		String ipLine = lines[1].trim();
		String portLine = lines[2].trim();
		int port;
		try {
			port = Integer.parseInt(portLine);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Port non-numérique: " + portLine, e);
		}

		return new HelloMessage(ipLine, port);
	}

	/*
	 * Sérialise le message HELLO en texte.
	 */

	public String serialize() {
		return String.format("HELLO%n%s%n%d%n", ip, port);
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "HelloMessage[ip=" + ip + ", port=" + port + "]";
	}

    // Méthode de test
    
	public static void main(String[] args) {
		String sample = "HELLO\n127.0.0.1\n4040\n";
		System.out.println("Sample message:\n" + sample);
		try {
			HelloMessage m = HelloMessage.parse(sample);
			System.out.println("Parsed: " + m);
			System.out.println("Serialized:\n" + m.serialize());
		} catch (IllegalArgumentException e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}
}



