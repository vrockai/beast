package edu.tuke.beast.input;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;

/**
 * <br>
 *
 * @author ivo Date: 11-Oct-2004 Time: 16:50:59
 */
public class Input {

    private final File corpusDir;
    //private TokenStream tokenStream;
    public static final String NULL_TOKEN = "#";
    
    public static final Logger logger = Logger.getRootLogger();

    public Input(File corpusDir) {
        this.corpusDir = corpusDir;
    }
    private PropertyChangeListener propertyChangeListener;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.propertyChangeListener = pcl;
    }

    public TokenStream getTokenStream() throws IOException {
        /**
         * create readers for files in the corpus directory then create Token
         * strem with analyzer
         */
        Collection<Reader> readers = indexDocs(new ArrayList<Reader>(),
                corpusDir);
        BeastAnalyzer analyzer = new BeastAnalyzer();
        analyzer.addPropertyChangeListener(propertyChangeListener);
        return analyzer.tokenStream(readers.toArray(new Reader[0]));
    }

    private Collection<Reader> indexDocs(Collection<Reader> readers, File file)
            throws IOException {
        // do not try to index files that cannot be read //do not index the cvs
        // directory
        if (file.canRead() && !"CVS".equals(file.getName())) {
            if (file.isDirectory()) {
                String[] files = file.list();
                //file.list();
                // an IO error could occur
                if (files != null) {
                    for (String file1 : files) {
                        indexDocs(readers, new File(file, file1));
                    }
                }
            } else {
                logger.info("adding " + file);

                //Reader reader = new FileReader(file);
                // nahrada tohto riadku aby cital diakritiku
                InputStream fis = new FileInputStream(file);                
                Reader reader = new InputStreamReader(fis, "UTF-8");
                readers.add(reader);
            }
        }
        return readers;
    }
}
