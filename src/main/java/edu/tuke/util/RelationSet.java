package edu.tuke.util;

/**
 * Date: 14.03.2005
 * Time: 17:48:20
 */
public class RelationSet {

    final static boolean VERBOSE = true;
//    public static int LEXICON_SLICE = 1000;
//
//    float[][] matrix = new float[LEXICON_SLICE][LEXICON_SLICE];
//
//    Cortex cortex;
//    Lexicon lexicon;
//
//    private HashMap<Token<Integer>, Integer> indexing;
//    private HashMap<Integer, Token<Integer>> indexingInverse;
//
//    public RelationSet(Cortex c, int slice) throws Exception {
//
//        LEXICON_SLICE = slice;
//        indexing = new HashMap<Token<Integer>, Integer>();
//        indexingInverse = new HashMap<Integer, Token<Integer>>();
//
//        cortex = c;
//        lexicon = c.getLexicon().top(LEXICON_SLICE);
//        System.out.println( lexicon.toString());
//
//        Stopwatch stopwatch = new Stopwatch();
//
//        stopwatch.start();
//        System.out.println("intializing matrix");
//
//        for (int i = 0; i < LEXICON_SLICE; i++)
//            for (int j = 0; j < LEXICON_SLICE; j++) {
//                matrix[i][j] = 0;
//            }
//        stopwatch.stop();
//        System.out.println("matrix cleaning time : " + stopwatch.getElapsedTime());
//        stopwatch.reset();
//
//        System.out.println("computing relativnes for the theshold :" + cortex.getTreshold());
//        System.out.println("0%                                                50%                                           100%");
//        System.out.println("----------------------------------------------------------------------------------------------------");
//
//
//        int i = 0;
//        for (Token<Integer> iToken : lexicon.getHashMap().keySet()) {
//            //stopwatch.start();
//            indexing.put(iToken, i);
//            indexingInverse.put(i, iToken);
//
//            int j = 0;
//            for (Token<Integer> jToken : lexicon.getHashMap().keySet()) {
//                if (j < i) {
//                    j++;
//                    continue;
//                }
//                matrix[i][j] = (float)(cortex.getSimilarity(iToken, jToken,1))*0.4f+
//                               (float)(cortex.getSimilarity(iToken, jToken,2))*0.3f+
//                               (float)(cortex.getSimilarity(iToken, jToken,3))*0.2f+
//                               (float)(cortex.getSimilarity(iToken, jToken,4))*0.1f;
//                j++;
//                if (j >= LEXICON_SLICE) break;
//            }
//
//
//            i++;
//            if (i >= LEXICON_SLICE) break;
//            if (i % ((float) LEXICON_SLICE / 100) == 0) System.out.print(".");
//            //stopwatch.stop();
//            //System.out.println(i + " time: "+stopwatch.getElapsedTime());
//        }
//        System.out.println("ok2");
//    }
//
//    public Set<Token<Integer>> getSlicedLexicon() {
//        return indexing.keySet();
//    }
//
//    public Lexicon getLexicon() {
//        return lexicon;
//    }
//
//    public int getIndex(Token<Integer> token) {
//        return indexing.get(token);
//    }
//
//    public Token<Integer> getToken(int i) {
//        return indexingInverse.get(i);
//    }
//
//    public SortedSet<ExcitedToken<Integer>> getRelationSet(Token<Integer> token) {
//
//        SortedSet<ExcitedToken<Integer>> resultSet = new TreeSet<ExcitedToken<Integer>>(new ExcitedTokenStrengthComparator());
//
//        int ind = getIndex(token);
//
//        for (int j = 0; j < ind; j++)
//        if (matrix[j][ind] > 0 )
//            resultSet.add(new ExcitedToken<Integer>(getToken(j).getValue(), matrix[j][ind]));
//        for (int i = ind; i < LEXICON_SLICE; i++)
//        if (matrix[ind][i] > 0 )
//            resultSet.add(new ExcitedToken<Integer>(getToken(i).getValue(), matrix[ind][i]));
//
//        return resultSet;
//    }
//
//    public float getIntegerRelatives(Token<Integer> iToken, Token<Integer> jToken) {
//        int iInd = indexing.get(iToken);
//        int jInd = indexing.get(jToken);
//
//        if (iInd == jInd)
//            return 0;
//
//        return matrix[Math.min(iInd, jInd)][Math.max(iInd, jInd)];
//    }
//
//    public float getObjectRelatives(Token<Object> iToken, Token<Object> jToken) {
//        return getIntegerRelatives(cortex.getLexicon().getIndex(iToken), cortex.getLexicon().getIndex(jToken));
//    }
//
//    public String toString() {
//
//        StringBuffer sb = new StringBuffer();
//
//        for (int j = 0; j < LEXICON_SLICE - 1; j++)
//            sb.append(lexicon.getEntry(indexingInverse.get(j)) + " ");
//        sb.append(lexicon.getEntry(indexingInverse.get(LEXICON_SLICE - 1)) + " ");
//
//        sb.append("\n");
//
//        for (int j = 0; j < LEXICON_SLICE; j++) {
//            for (int i = 0; i < LEXICON_SLICE - 1; i++)
//                sb.append(matrix[i][j] + " ");
//            sb.append(matrix[LEXICON_SLICE - 1][j] + " ");
//            if (j!= LEXICON_SLICE-1)
//                sb.append("\n");
//        }
//
//        return sb.toString();
//    }
}
