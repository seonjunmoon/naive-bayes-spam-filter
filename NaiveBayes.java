// Do not submit with package statements if you are using eclipse.
// Only use what is provided in the standard libraries.

import java.io.*;
import java.util.*;

public class NaiveBayes {

    /*
     *
     * Train your Naive Bayes Classifier based on the given training
     * ham and spam emails.
     *
     * Params:
     *      hams - email files labeled as 'ham'
     *      spams - email files labeled as 'spam'
     */

    Set<String> spamSet = new HashSet<>();
    Set<String> hamSet = new HashSet<>();
    Map<String, Double> spamMap = new HashMap<>();
    Map<String, Double> hamMap = new HashMap<>();
    Map<Integer, Set<String>> wordMapS = new HashMap<>();
    Map<Integer, Set<String>> wordMapH = new HashMap<>();
    Set<String> wordSetS = new HashSet<>();
    Set<String> wordSetH = new HashSet<>();
    Map<Integer, Double> probMap = new HashMap<>();
    double probOfSpam;
    double probOfHam;

    public void train(File[] hams, File[] spams) throws IOException {

        for (int i = 0; i < spams.length; i++) {
            wordSetS = tokenSet(spams[i]);
            wordMapS.put(i, wordSetS);
            for (String w : wordSetS) {
                spamSet.add(w);
            }
        }

        for (String s : spamSet) {
            double num = 0;
            for (int i = 0; i < spams.length; i++) {
                if (wordMapS.get(i).contains(s)) {
                    num++;
                }
            }
            spamMap.put(s, (num + 1) / ((double) (spams.length + 2)));
        }

        for (int i = 0; i < hams.length; i++) {
            wordSetH = tokenSet(hams[i]);
            wordMapH.put(i, wordSetH);
            for (String w : wordSetH) {
                hamSet.add(w);
            }
        }

        for (String s : hamSet) {
            double num = 0;
            for (int i = 0; i < hams.length; i++) {
                if (wordMapH.get(i).contains(s)) {
                    num++;
                }
            }
            hamMap.put(s, (num + 1) / ((double) (hams.length + 2)));
        }
        for (String s : hamMap.keySet()) {
            if (!spamMap.containsKey(s)) {
                spamMap.put(s, 0.0);
                spamMap.put(s, 1 / ((double) (spams.length + 2)));
            }
        }
        for (String s : spamMap.keySet()) {
            if (!hamMap.containsKey(s)) {
                hamMap.put(s, 0.0);
                hamMap.put(s, 1 / ((double) (hams.length + 2)));
            }
        }

        probOfSpam = ((double) (spams.length)) / ((double) (spams.length + hams.length));
        probOfHam = ((double) (hams.length)) / ((double) (spams.length + hams.length));
    }

    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     *
     * Classify the given unlabeled set of emails. Add each email to the correct
     * label set. SpamFilterMain.java would follow the format in
     * example_output.txt and output your result to stdout. Note the order
     * of the emails in the output does NOT matter.
     *
     *
     * Params:
     *      emails - unlabeled email files to be classified
     *      spams  - set for spam emails that needs to be populated
     *      hams   - set for ham emails that needs to be populated
     */
    public void classify(File[] emails, Set<File> spams, Set<File> hams) throws IOException {

        for (int i = 0; i < emails.length; i++) {
            Set<String> wordSet = tokenSet(emails[i]);
            double spamChunk = 0;
            double hamChunk = 0;
            for (String w : wordSet) {
                if (spamMap.containsKey(w)) {
                    spamChunk += Math.log(spamMap.get(w));
                }
                if (hamMap.containsKey(w)) {
                    hamChunk += Math.log(hamMap.get(w));
                }
            }
            double left = Math.log(probOfSpam) + spamChunk;
            double right = Math.log(probOfHam) + hamChunk;
            if (left > right) {
                spams.add(emails[i]);
            } else {
                hams.add(emails[i]);
            }
        }
    }


    /*
     *  Helper Function:
     *  This function reads in a file and returns a set of all the tokens.
     *  It ignores "Subject:" in the subject line.
     *
     *  If the email had the following content:
     *
     *  Subject: Get rid of your student loans
     *  Hi there ,
     *  If you work for us , we will give you money
     *  to repay your student loans . You will be
     *  debt free !
     *  FakePerson_22393
     *
     *  This function would return to you
     *  ['be', 'student', 'for', 'your', 'rid', 'we', 'of', 'free', 'you',
     *   'us', 'Hi', 'give', '!', 'repay', 'will', 'loans', 'work',
     *   'FakePerson_22393', ',', '.', 'money', 'Get', 'there', 'to', 'If',
     *   'debt', 'You']
     */
    public static HashSet<String> tokenSet(File filename) throws IOException {
        HashSet<String> tokens = new HashSet<String>();
        Scanner filescan = new Scanner(filename);
        filescan.next(); // Ignoring "Subject"
        while (filescan.hasNextLine() && filescan.hasNext()) {
            tokens.add(filescan.next());
        }
        filescan.close();
        return tokens;
    }
}
