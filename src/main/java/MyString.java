public class MyString {
    private String string = "";
    private int padding = 60;
    private String fillChar = "-";

    public MyString (String str) {
        this.string = str;
        string = padding(padding, fillChar);

    }
    public MyString (String str, String str2) {
        this.string = str;
        string = padding(padding, fillChar);
        string += str2;
    }

    public MyString (String str, int count) {
        this.string = str;
        string = padding(padding, fillChar);
        string += count;
    }

    public MyString (String str, String str2, int count) {
        this.string = str;
        string = padding(padding, fillChar);

        string += str2 + " " + count;
    }

    public MyString (String str, String str2, String str3) {
        this.string = str;
        string = padding(padding, fillChar);
        string += str2 + str3;
    }


    public String padding(int length, String fillChar) {
        int lenString = string.length();
        int fillLength = length - lenString;

        String fillString = "";

        for(int i=0; i < fillLength; i++) fillString += fillChar;

        return string+fillString+" ";

    }
    public String toString() {
        return ""+string;
    }

}
