import java.io.BufferedReader;
import java.io.InputStreamReader; 
import java.io.FileInputStream;
import java.io.BufferedOutputStream; 
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter; 

class ramon_p2{
    public static void main(String[] args){
        if (args.length == 3){
            if( args[0].startsWith("t")){
                convertTextToBinary(args[1], args[2]);
            } else if (args[0].startsWith("b")){
                convertBinaryToText(args[1], args[2]);
            } else{
                System.out.println("First argument must start with 'b' or 't'");
                System.exit(0);
            }
        }
        else{
            System.out.println("Please input THREE commandline arguments");
            System.exit(0);
        }
    }

    private static void convertTextToBinary(String inputFileName, String outputFileName){
        try{
            // opens input & output 
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName)));
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFileName));
           
            // initiating new array
            ArrayList<String> lines =  new ArrayList<>();

            // initiating new string 
            String line; 

            int byteCount = 0; 

            // loop to read each line of text file and adds to lines ArrayList 
            while((line = input.readLine()) != null)
            {
                lines.add(line);
                byteCount++;
            }

            // stores total # of lines inside text file
            int totalLines = lines.size();

            // create new byte array w/size of 8 
            byte[] b = new byte[8];
            ByteBuffer bb = ByteBuffer.wrap(b);
            
            // write total # of lines to binary file 
            bb.putInt(totalLines);
            output.write(b, 0, 4);

            for (int i = 0; i < lines.size(); i++){
                // gets current text block/line
                String inn = lines.get(i);

                // splits text block into two blocks: type & data 
                StringTokenizer st = new StringTokenizer(inn, "\t");
                String type = st.nextToken();
                String data = st.nextToken();

                if (type.equals("int")){
                    bb.putChar(0, 'i');
                    output.write(b, 0, 2);
                    bb.putInt(0, Integer.parseInt(data));
                    output.write(b, 0, 4);
                } 
                else if (type.equals("long")){
                    bb.putChar(0, 'l');
                    output.write(b, 0, 2);
                    bb.putLong(0, Long.parseLong(data));
                    output.write(b, 0, 8);
                } 
                else if (type.equals("short")){
                    bb.putChar(0, 'h');
                    output.write(b, 0, 2);
                    bb.putShort(0, Short.parseShort(data));
                    output.write(b, 0, 2);
                } 
                else if (type.equals("float")){
                    bb.putChar(0, 'f');
                    output.write(b, 0, 2);
                    bb.putFloat(0, Float.parseFloat(data));
                    output.write(b, 0, 4);
                } 
                else if (type.equals("double")){
                    bb.putChar(0, 'd');
                    output.write(b, 0, 2);
                    bb.putDouble(0, Double.parseDouble(data));
                    output.write(b, 0, 8);
                } 
                else if (type.equals("string")){
                    bb.putChar(0, 's');
                    output.write(b, 0, 2);
                    bb.putInt(0, data.length());
                    output.write(b, 0, 4); 
                    char[] chArr = data.toCharArray();
                    for (int r = 0; r < chArr.length; r++){
                        bb.putChar(0, chArr[r]);
                        output.write(b, 0, 2);
                    }
                } 
                else if (type.equals("long array")){
                    bb.putChar(0, 'b');
                    output.write(b, 0, 2);
                    String[] dataArr = data.split(",");
                    int no = dataArr.length;
                    bb.putInt(0, no);
                    output.write(b, 0, 4);
                    for (int u = 0; u < no; u++){
                        long yt = Long.parseLong(dataArr[u]);
                        bb.putLong(0, yt);
                        output.write(b, 0, 8);
                    }
                } 
                else if (type.equals("float array")){
                    bb.putChar(0, 'g');
                    output.write(b, 0, 2);
                    String[] dataArry = data.split(",");
                    int po = dataArry.length;
                    bb.putInt(0, po);
                    output.write(b, 0, 4);
                    for (int e = 0; e < po; e++){
                        float co = Float.parseFloat(dataArry[e]);
                        bb.putFloat(0, co);
                        output.write(b, 0, 4);
                    }
                }
            }
            System.out.println("byteCount = " + byteCount);
            input.close();
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            System.exit(0);
        }
    }

    private static void convertBinaryToText(String inputFileName, String outputFileName){  
        try{
            DataInputStream input = new DataInputStream(new FileInputStream(inputFileName));
            PrintWriter output =new PrintWriter(new BufferedWriter(new FileWriter(outputFileName)));
            
            int m = input.readInt();
            
            for(int h = 0; h < m; h++){
                char type = input.readChar();

                if (type == 'i'){
                    int v = input.readInt();
                    output.println("int\t" + v);
                } 
                else if (type == 'l'){ 
                    long v = input.readLong();
                    output.println("long\t" + v);
                } 
                else if (type == 'h'){
                    short v = input.readShort();
                    output.println("short\t" + v);
                } 
                else if (type == 'f'){
                    float v = input.readFloat();
                    output.println("float\t" + v);
                } 
                else if(type == 'd'){
                    double v = input.readDouble();
                    output.println("double\t" + v);
                }
                else if (type == 's'){
                    int length = input.readInt();
                    String v = "";
                    for (int g = 0; g < length; g++){
                        v = v + input.readChar();
                    }
                    output.println("string\t" + v);
                } 
                else if (type == 'h'){
                    short v = input.readShort();
                    output.println("short\t" + v);
                } 
                else if (type == 'b'){
                    int length = input.readInt();
                    long[] v = new long[length];
                    for (int t = 0; t < length; t++){
                        v[t] = input.readLong();
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int k = 0; k < v.length; k++){
                        sb.append(v[k]);
                        if (k < v.length -1){
                            sb.append(",");
                        }
                    }
                    output.println("long array\t" + sb.toString());
                } 
                else if (type == 'g'){
                    int length = input.readInt();
                    float[] v = new float[length];
                    for (int c = 0; c < length; c++){
                        v[c] = input.readFloat();
                    }
                    StringBuilder sb = new StringBuilder();
               
                    for (int k = 0; k < v.length; k++){
                        sb.append(v[k]);
                        if (k < v.length -1){
                            sb.append(",");
                        }
                    }
                    output.println("float array\t" + sb.toString());
                }
            }
            input.close();
            output.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            System.exit(0);
        }
    }
}