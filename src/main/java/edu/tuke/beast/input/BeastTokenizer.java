package edu.tuke.beast.input;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.Token;
import java.io.Reader;

/**
 * <br>
 *
 * @author ivo Date: 11-Oct-2004 Time: 15:55:21
 */
class BeastTokenizer extends Tokenizer {

    private boolean wasNullToken = false;
    private int readerIndex = 1;
    private int status = 0;
    private final Reader[] readers;
    private PropertyChangeListener propertyChangeListener;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.propertyChangeListener = pcl;
    }

    public BeastTokenizer(Reader[] input) {
        super(input[0]);
        readers = input;
    }

    public BeastTokenizer(Reader input) {
        super(input);
        readers = new Reader[]{input};
    }
    private int offset = 0,  bufferIndex = 0,  dataLen = 0;
    private static final int MAX_WORD_LEN = 255;
    private static final int IO_BUFFER_SIZE = 16;
    private final char[] buffer = new char[MAX_WORD_LEN];
    private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

    //private static final String null_tokens = "?!\"?$%&/()=?`*\'_:;,.-#+~{[]}\\??";
    /**
     * Called on each token character to normalize it before it is added to the
     * token. The default implementation does nothing. Subclasses may use this
     * to, e.g., lowercase tokens.
     */
    /** Returns the next token in the stream, or null at EOS. */
    @Override
    public final Token next(Token reusableToken) throws java.io.IOException {
        int length = 0;
        int start = offset;

        if (wasNullToken) {
            wasNullToken = false;
            // next();
            return new Token(Input.NULL_TOKEN, start, start + length);
        // return null;
        }

        while (true) {
            final char c;

            offset++;

            if (bufferIndex >= dataLen) {
                dataLen = input.read(ioBuffer);
                bufferIndex = 0;
            }

            // dataLen = -1 if the end of the stream was 
            if (dataLen == -1) {
                if (length > 0) {
                    break;
                } else if (readerIndex >= readers.length) {
                    return null;
                } else {
                    input = readers[readerIndex++];

                    this.status = (int) ((double) 100 * ((double) readerIndex / (double) readers.length));
                    if (propertyChangeListener != null) {
                        PropertyChangeEvent pce = new PropertyChangeEvent(this, "status", this.status - 1, this.status);
                        propertyChangeListener.propertyChange(pce);
                    }
                    return new Token(Input.NULL_TOKEN, start, start + length);
                }
            } else {
                c = ioBuffer[bufferIndex++];
            }

            // if token is whitespace
            if (isNullToken(c)) {
                wasNullToken = length != 0;
                return wasNullToken ? new Token(new String(buffer, 0, length), start,
                        start + length) : new Token(Input.NULL_TOKEN, start, start + length);
            }
            // if it's a token char
            if (!Character.isWhitespace(c)) {

                if (length == 0) // start of token
                {
                    start = offset - 1;
                }

                buffer[length++] = c; // buffer it, normalized

                if (length == MAX_WORD_LEN) // buffer overflow!
                {
                    break;
                }

            } else if (length > 0) // at non-Letter w/ chars
            {
                break; // return 'em
            }
        }
        reusableToken = new Token(new String(buffer, 0, length), start, start + length);
        return reusableToken;
    }

    private boolean isNullToken(char c) {
/*
        if (c == '\'') {
            return false;
        }
*/
        if (c == '@') {
            return false;
        }

        return !Character.isWhitespace(c) && !Character.isLetter(c);
    }
}