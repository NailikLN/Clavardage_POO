package Communication;

import java.net.*;


public class TypeOfMessage
{
    protected final int port;
    protected final InetAddress adress;
    protected final TypeMessage type;
    public final int LENGTH_BUFFER = 256;
    public enum TypeMessage
    {
        Connect,
        Disconnect,
        ChangeName,
        WrongType;

        private static TypeMessage from_byte(byte value)
        {
            switch(value){
                case 0x01 -> {return Connect;}
                case 0x02 -> {return Disconnect;}
                case 0x03 -> {return ChangeName;}
                default -> {return WrongType;}
            }
        }

        public byte to_byte()
        {
            switch (this){
                case Connect -> {return 0x01;}
                case Disconnect -> {return 0x02;}
                case ChangeName-> {return 0x03;}
                default -> {return 0x00;}
            }
        }
    }

    public TypeOfMessage(int port, InetAddress adress, TypeMessage type)
    {
        this.port = port;
        this.adress = adress;
        this.type = type;
    }

    public TypeOfMessage(DatagramPacket packet)
    {
        this.port = packet.getPort();
        this.adress = packet.getAddress();
        this.type = TypeMessage.from_byte(packet.getData()[0]);
    }

    public TypeMessage getType()
    {
        return this.type;
    }

    public InetAddress getAdress()
    {
        return this.adress;
    }

    public int getPort()
    {
        return this.port;
    }

    public DatagramPacket to_packet(){
        byte[] buffer = new byte[LENGTH_BUFFER];
        buffer[0] = this.type.to_byte();
        DatagramPacket packet = new DatagramPacket(buffer, LENGTH_BUFFER);
        packet.setAddress(adress);
        packet.setPort(port);
        return packet;
    }

    public static TypeOfMessage from_packet(DatagramPacket packet){
        switch(TypeMessage.from_byte(packet.getData()[0])){
            case Connect -> {return new ConnectMessage(packet);}
            case Disconnect -> {return new DisconnectMessage(packet);}
            case ChangeName -> {return new ChangeName(packet);}
            default -> {return new WrongType(packet);}
        }
    }

    public String toString(){
        return "Type :" + type;
    }
}
