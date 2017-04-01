package pcapReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jnetpcap.Pcap;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JFlow;
import org.jnetpcap.packet.JFlowKey;
import org.jnetpcap.packet.JFlowMap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Ip6;

import static pcapReader.testingC.runFile;

/**
 * Created by Mark-Laptop on 2017-03-30.
 */
public class testingC {
    private static String FILENAME;
    private static int[] tcp_count = {0};
    private static int[] udp_count = {0};
    private static int[] arp_count = {0};
    private static int[] ip4_count = {0};

    private static int[] igmp_count = {0};

    public static void main(String[] args) {
        //For Testing purposes
        String fileN = "tests/random_test.pcap";
        setFilename(fileN);

        runFile();
        System.out.println("TCP count is: " + getCount_tcp());
        System.out.println("UDP count is: " + getCount_udp());
        System.out.println("ARP count is: " + getCount_arp());
        System.out.println("IP4 count is: " + getCount_ip4());


    }

    public static void setFilename(String filename){
        FILENAME = filename;
    }

    public static String getFilename(){
        return FILENAME;
    }

    public static void runFile(){
        String FILENAME = getFilename();
        final StringBuilder errbuf = new StringBuilder();

        final Pcap pcap = Pcap.openOffline(FILENAME, errbuf);
        if (pcap == null) {
            System.err.println(errbuf);
            return;
        }

        pcap.loop(Pcap.LOOP_INFINITE, new JPacketHandler<StringBuilder>() {
            final Tcp tcp = new Tcp();
            final Udp udp = new Udp();
            final Arp arp = new Arp();
            final Ip4 ip4 = new Ip4();

            public void nextPacket(JPacket packet, StringBuilder errbuf) {
                if (packet.hasHeader(tcp)) {
                    tcp_count[0] = tcp_count[0] + 1;
                }
                if (packet.hasHeader(udp)) {
                    udp_count[0] = udp_count[0] + 1;
                }
                if (packet.hasHeader(arp)) {
                    arp_count[0] = arp_count[0] + 1;
                }
                if (packet.hasHeader(ip4)) {
                    ip4_count[0] = ip4_count[0] + 1;
                }
            }
        }, errbuf);
        setTCP_count(tcp_count);
        setUDP_count(udp_count);
        setARP_count(arp_count);
        setIP4_count(ip4_count);
        pcap.close();
    }
    //TCP
    public static void setTCP_count(int[] count){
        tcp_count = count;
    }

    public static int getCount_tcp(){
        return tcp_count[0];
    }

    //UDP
    public static void setUDP_count(int[] count){
        udp_count = count;
    }

    public static int getCount_udp(){
        return udp_count[0];
    }

    //ARP
    public static void setARP_count(int[] count){
        arp_count = count;
    }

    public static int getCount_arp(){
        return arp_count[0];
    }

    //IP4
    public static void setIP4_count(int[] count){
        ip4_count = count;
    }

    public static int getCount_ip4(){
        return ip4_count[0];
    }

}
