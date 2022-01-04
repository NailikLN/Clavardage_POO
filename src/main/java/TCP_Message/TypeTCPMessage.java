package TCP_Message;

public class TypeTCPMessage {

    private TypeMessage typeMessage;

    public TypeTCPMessage(TypeMessage typeMessage) {
        this.typeMessage = typeMessage;
    }

    public enum TypeMessage
    {
        MESSAGE,
        WRONG_TYPE;

        private static TypeMessage from_byte(byte value)
        {
            switch(value){
                case 0x01 -> {return MESSAGE;}
                default -> {return WRONG_TYPE;}
            }
        }

        public byte to_byte()
        {
            switch (this){
                case MESSAGE -> {return 0x01;}
                default -> {return 0x00;}
            }
        }
    }

    public  TypeMessage getTypeMessage()
    {
        return typeMessage;
    }


}
