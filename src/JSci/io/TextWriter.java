package JSci.io;

import java.io.*;
import JSci.maths.*;

/**
* Text writer, writes data text files/streams.
* This class uses buffered I/O.
* @version 1.7
* @author Mark Hale
*/
public final class TextWriter extends OutputStreamWriter {
        private final BufferedWriter writer=new BufferedWriter(this);
        private char delimiter;
        /**
        * Writes text data to an output stream.
        */
        public TextWriter(OutputStream stream) {
                super(stream);
        }
        /**
        * Writes to a text file with the specified system dependent file name.
        * @param name the system dependent file name.
        * @param ch the character that delimits data columns.
        * @exception IOException If the file is not found.
        */
        public TextWriter(String name,char ch) throws IOException {
                super(new FileOutputStream(name));
                delimiter=ch;
        }
        /**
        * Writes to a text file with the specified File object.
        * @param file the file to be opened for writing.
        * @param ch the character that delimits data columns.
        * @exception IOException If the file is not found.
        */
        public TextWriter(File file,char ch) throws IOException {
                super(new FileOutputStream(file));
                delimiter=ch;
        }
        /**
        * Writes a single character.
        * @exception IOException If an I/O error occurs.
        */
        public void write(int c) throws IOException {
                writer.write(c);
        }
        /**
        * Writes a string.
        * @exception IOException If an I/O error occurs.
        */
        public void write(String str) throws IOException {
                writer.write(str);
        }
        /**
        * Close the stream.
        * @exception IOException If an I/O error occurs.
        */
        public void close() throws IOException {
                writer.flush();
                super.close();
        }
        /**
        * Writes an array of data.
        * @param data the data to be written.
        * @exception IOException If an I/O error occurs.
        */
        public void write(double data[]) throws IOException {
                int i;
                for(i=0;i<data.length-1;i++)
                        write(data[i]+" "+delimiter+" ");
                write(data[i]+"\n");
        }
        /**
        * Writes an array of data.
        * @param data the data to be written.
        * @exception IOException If an I/O error occurs.
        */
        public void write(double data[][]) throws IOException {
                for(int j,i=0;i<data.length;i++) {
                        for(j=0;j<data[i].length-1;j++)
				write(data[i][j]+" "+delimiter+" ");
                        write(data[i][j]+"\n");
                }
        }
        /**
        * Writes an array of data.
        * @param data the data to be written.
        * @exception IOException If an I/O error occurs.
        */
        public void write(int data[]) throws IOException {
                int i;
                for(i=0;i<data.length-1;i++)
                        write(data[i]+" "+delimiter+" ");
                write(data[i]+"\n");
        }
        /**
        * Writes an array of data.
        * @param data the data to be written.
        * @exception IOException If an I/O error occurs.
        */
        public void write(int data[][]) throws IOException {
                for(int j,i=0;i<data.length;i++) {
                        for(j=0;j<data[i].length-1;j++)
				write(data[i][j]+" "+delimiter+" ");
                        write(data[i][j]+"\n");
                }
        }
        /**
        * Writes a matrix.
        * @param matrix the matrix to be written.
        * @exception IOException If an I/O error occurs.
        */
        public void write(DoubleMatrix matrix) throws IOException {
                final int mRow=matrix.rows();
                final int mCol=matrix.columns();
                for(int j,i=0;i<mRow;i++) {
                        for(j=0;j<mCol-1;j++)
				write(matrix.getElement(i,j)+" "+delimiter+" ");
                        write(matrix.getElement(i,j)+"\n");
                }
        }
        /**
        * Writes a matrix.
        * @param matrix the matrix to be written.
        * @exception IOException If an I/O error occurs.
        */
        public void write(IntegerMatrix matrix) throws IOException {
                final int mRow=matrix.rows();
                final int mCol=matrix.columns();
                for(int j,i=0;i<mRow;i++) {
                        for(j=0;j<mCol-1;j++)
				write(matrix.getElement(i,j)+" "+delimiter+" ");
                        write(matrix.getElement(i,j)+"\n");
                }
        }
}

