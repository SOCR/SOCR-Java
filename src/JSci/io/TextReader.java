package JSci.io;

import java.io.*;
import java.util.Vector;
import JSci.maths.*;

/**
* Text reader, reads data text files/streams.
* This class uses buffered I/O.
* @version 1.7
* @author Mark Hale
*/
public final class TextReader extends InputStreamReader {
        private final BufferedReader reader=new BufferedReader(this);
        /**
        * Reads text data from an input stream.
        */
        public TextReader(InputStream stream) {
                super(stream);
        }
        /**
        * Reads a text file with the specified system dependent file name.
        * @param name the system dependent file name.
        * @exception FileNotFoundException If the file is not found.
        */
        public TextReader(String name) throws FileNotFoundException {
                super(new FileInputStream(name));
        }
        /**
        * Reads a text file with the specified File object.
        * @param file the file to be opened for reading.
        * @exception FileNotFoundException If the file is not found.
        */
        public TextReader(File file) throws FileNotFoundException {
                super(new FileInputStream(file));
        }
        /**
        * Read a single character.
        * @exception IOException If an I/O error occurs.
        */
        public int read() throws IOException {
                return reader.read();
        }
        /**
        * Reads data to an array.
        * @exception IOException If an I/O error occurs.
        */
        public double[][] readArray() throws IOException {
                final Vector data=new Vector(10);
                for(int ch=read();ch!='\n' && ch!=-1;) {
                        if(isNumber(ch)) {
                                StringBuffer str=new StringBuffer();
                                do {
                                        str.append((char)ch);
                                        ch=read();
                                } while(isNumber(ch));
                                data.addElement(str.toString());
                        }
                        while(!isNumber(ch) && ch!='\n' && ch!=-1)
                                ch=read();
                }
                int cols=data.size();
                int rows=1;
                for(int ch=read();ch!=-1;) {
                        if(isNumber(ch)) {
                                StringBuffer str=new StringBuffer();
                                do {
                                        str.append((char)ch);
                                        ch=read();
                                } while(isNumber(ch));
                                data.addElement(str.toString());
                        }
                        while(!isNumber(ch) && ch!='\n' && ch!=-1)
                                ch=read();
                        if(ch=='\n') {
                                ch=read();
                                rows++;
                        }
                }
                double array[][]=new double[rows][cols];
                for(int j,i=0;i<rows;i++) {
                        for(j=0;j<cols;j++)
                                array[i][j]=Double.parseDouble(data.elementAt(i*cols+j).toString());
                }
                return array;
        }
        private boolean isNumber(int ch) {
                if(Character.isDigit((char)ch) || ch=='.' || ch=='+' || ch=='-' || ch=='e' || ch=='E')
                        return true;
                else
                        return false;
        }
}

