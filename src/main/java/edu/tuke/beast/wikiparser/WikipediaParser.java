package edu.tuke.beast.wikiparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class WikipediaParser extends HTMLEditorKit.ParserCallback {

    public String url = null;
    public String word = "";

    public static String URL_WIKIPEDIA = "http://en.wiktionary.org/";
    public static String URL_WIKTIONARY = "http://en.wikipedia.org/";

    public String urlPfx = "";//http://en.wiktionary.org/";
    //public String urlPfx = "http://en.wikipedia.org/";
    public String urlCen = "wiki/";
    public String charset = null;
    public String actCatSuffix = "";
    public boolean isPar = false;
    public boolean isDivContent = false;
    public boolean isDivCategories = false;
    public boolean isCategoryLink = false;
    public int divLevel = 0;
    private final StringBuffer sb = new StringBuffer();
    private Map<String,String> categories = new HashMap<String,String>();

    public String getUrlPfx() {
        return urlPfx;
    }

    public void setUrlPfx(String urlPfx) {
        this.urlPfx = urlPfx;
    }

    public String getContent() {
        return sb.toString();
    }

    public Map<String,String> getCategories() {
        return categories;
    }

    /**
     *
     * @param word
     * @param charset
     * konstruktor tridy HtmlToText
     */
    public WikipediaParser() {
        this.charset = "UTF-8";
    }

    public WikipediaParser(String url) {
        this.charset = "UTF-8";
        this.urlPfx = url;
    }

    public void parse(){
        sb.delete(0, sb.length());
        categories.clear();
        run_url();
    }

    private String getUrl(String suffix){
        return urlPfx+urlCen+suffix;
    }

    public void setWord(String word){
        this.word = word;
        this.url = getUrl(word);
    }

    public String getWord(){
        return word;
    }

    public void setCharset(String charset){
        this.charset = charset;
    }

    public String getCharset(){
        return charset;
    }

    public WikipediaParser(String word, String charset) {
        //jednoducha kontrola url adresy, jestli je ve tvaru s HTTP://

        String url = getUrl(word);
        
        if (this.check_url(url.trim()) == 0) {
            System.out.println("Url adresa neni ve spravnem tvaru. Prosim zadejte adresu ve tvaru http://");
            return;
        }

        //nastaveni promennych
        this.url = url;
        this.charset = charset;
        //spusteni parseru
        run_url();
    }

    /**
     * zacatek parsovani. Vytvori bufferdreader a spusti parser
     */
    private void run_url() {
        try {
            //vytvoreni nove URL
            URL url_id = new URL(this.url);
            //Vytvoreni BufferedReaderu
            BufferedReader htmlPage = new BufferedReader(new InputStreamReader(
                    url_id.openStream(), this.charset));
            //spusteni parsovani
            this.parse(htmlPage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param url
     * @return
     * jednoducha kontrola url adresy
     */
    public int check_url(String url) {
        if (!url.startsWith("http://")) {
            return 0;
        }
        return 1;
    }

    /**
     *
     * @param htmlText
     * @throws IOException
     * vytvori novy ParserDelegator a vola metodu pro parsovani
     */
    public void parse(Reader htmlText) throws IOException {
        ParserDelegator delegator = new ParserDelegator();
        delegator.parse(htmlText, this, Boolean.TRUE);
    }

    /**
     * @param text
     * @param pos
     * handleText odchiti veskery text, ktery vytvari obsah html stranky
     */
    @Override
    public void handleText(char[] text, int pos) {
        if (isPar && isDivContent && divLevel > 0) {
            sb.append(text);
        }

        if (isDivCategories && isCategoryLink){
            categories.put(new String(text),actCatSuffix);
        }
    }

    /**
     * @param coment
     * @param pos
     * handleComent odchiti html poznamky
     */
    @Override
    public void handleComment(char[] coment, int pos) {
        //System.out.println("Komentar: " + String.valueOf(coment));
    }

    /**
     * @param tag
     * @param atr
     * @param pos
     * handleStartTag odchyti zacatek noveho tagu, vcetne jeho atributu
     */
    @Override
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet atr, int pos) {

        if (tag.equals(HTML.Tag.P)) {
            isPar = true;
        }
        if ((tag.equals(HTML.Tag.DIV)) && ("content".equals(atr.getAttribute(HTML.Attribute.ID)))) {
            isDivContent = true;
            divLevel++;
        }
        if ((tag.equals(HTML.Tag.DIV)) && ("mw-normal-catlinks".equals(atr.getAttribute(HTML.Attribute.ID)))) {
            isDivCategories = true;
            divLevel++;
        }
        if ((tag.equals(HTML.Tag.A)) && ((""+atr.getAttribute(HTML.Attribute.TITLE)).startsWith("Category:"))) {
            isCategoryLink = true;
            actCatSuffix = ""+atr.getAttribute(HTML.Attribute.HREF);
        }
        if ((tag.equals(HTML.Tag.DIV)) && isDivContent && divLevel > 0) {
            divLevel++;
            // System.out.println(divLevel);
        }
    }

    /**
     * @param tag
     * @param pos
     * handleEndTag odchyti konec tagu
     */
    @Override
    public void handleEndTag(HTML.Tag tag, int pos) {
        if (tag.equals(HTML.Tag.P)) {
            isPar = false;
        }
        if ((tag.equals(HTML.Tag.DIV)) && isDivContent) {
            divLevel--;
        }
        if ((tag.equals(HTML.Tag.DIV)) && isDivCategories) {
            isDivCategories = false;
            divLevel--;
        }
        if ((tag.equals(HTML.Tag.A)) && isCategoryLink) {
            isCategoryLink = false;
        }
    }

    /**
     * @param tag
     * @param atr
     * @param pos
     * handleSimpleTag odchyti jednoduche html tagy jako BR, IMG, apod.
     */
    @Override
    public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet atr, int pos) {
        //System.out.println("Zacatek jednoducheho tagu: \"" + tag + "\"");
        //System.out.println("Tag obsahuje atributy: \"" + atr + "\"");
    }

    /**
     * @param str
     * @param pos
     * handleError odchyti chby v HTML
     */
    @Override
    public void handleError(String str, int pos) {
        //System.out.println("Parse error: " + str);
    }

    /**
     * @author Zachar Jiří
     * @param args
     */
    public static void main(String[] args) {
        String charset = "UTF-8";
        String url = "Dog";
        if (args.length > 0) {
            url = String.valueOf(args[0]);
        }
        if (args.length > 1) {
            charset = String.valueOf(args[1]);
        }
        WikipediaParser htmlParse = new WikipediaParser();
        htmlParse.setWord("yellow");
        htmlParse.parse();
        System.out.println(htmlParse.getCategories());
    }
}
