package postbacchatbot.postbacchatbot;


import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import postbacchatbot.postbacchatbot.model.Answer;
import postbacchatbot.postbacchatbot.model.Message;
import postbacchatbot.postbacchatbot.repository.AnswerRepository;
import postbacchatbot.postbacchatbot.repository.MessageRepository;
import postbacchatbot.postbacchatbot.repository.SchoolRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class OpennlpController {

    @Autowired
    public SchoolRepository sr;

    @Autowired
    public MessageRepository mr;


    @Autowired
    public AnswerRepository ar ;


    private static Map<String, String> questionAnswer = new HashMap<>();


    static {
        questionAnswer.put("greeting", "Hi, i'm Chatbot ask me a question!");
        questionAnswer.put("options-inquiry",
                "If you choose Literature : Baccalaureate L. If you choose Science : Option SVT, Option Science Physique, Option Science Maths A et B");
        questionAnswer.put("scientifique-inquiry", "The options are : Engineering, Medical School,Pharmacy,Architecture,Veterinary");
        questionAnswer.put("conversation-continue", "What else can I help you with?");
        questionAnswer.put("conversation-complete", "Nice chatting with you. Bbye.");
        questionAnswer.put("conversation-error", "I don't understand please ask another question");
        questionAnswer.put("science-math", "you can do engineering ,medecine,pharmacy,military,architecture as best options for your profil");
        questionAnswer.put("science-physics", "you can do engineering ,medecine,pharmacy,military,architecture ,technicien ,tourisme,nursery as best options for your profil");
        questionAnswer.put("science-economie", "you can do commerce schools  like ;encg and iscae or economie cpge");
        questionAnswer.put("description-ensam", "Ensam is a  public engineering school in casablanca and meknes that get you an engineering master degree in many options like;mechanical engineering,electric,informatique...");
        questionAnswer.put("description-ensa", "Ensam is a public engineering school in 10 cities in morocco that get you an engineering master degree in many options like;mechanical engineering,electric,informatique...");
        questionAnswer.put("description-iscae", "it is a public commerce school  in Rabat ,where you study 5 years to get a economy measter degree ");
        questionAnswer.put("description-encg", "it is a public commerce school build in more than 12 cities in morocco ,where you study 5 years to get a economy measter degree");
        questionAnswer.put("description-cpge", "it is 2 years of preparator classes where you prepare yourself to get examinated on CNC and have access to highest engineering schools ike ;EMI,ENIME ...");
        questionAnswer.put("ecole-commerce", "the commerce schools in morocco are: encg and iscae or do economy cpge  for 2 years");
        questionAnswer.put("ecole-ingenierie", "the engineering schools in morocco are: ensam and ensa or do mp or psi cpge for 2 years");
        questionAnswer.put("ecole-medecine", "the medecine schools in morocco are: public medecine universities localisated in more than 10 cities in morroco");
        questionAnswer.put("ecole-architecture", "the architecture schools in morocco are: ENA oujda,rabat ");
        questionAnswer.put("ecole-infermerie", "the nursery schools in morocco are: Ispits that is built in more than 10 cities in morocco");
        questionAnswer.put("ecole-pharmacie", "the pharmacy schools in morocco are: FMP built in more than 5 cities in morocco as tanger casa and rabat");
        questionAnswer.put("ecole-technicien", "the technical schools in morocco are:ofppt built in more than 10 cities in morocco in all fields and Ifmeree for energetic field and Ifmia for cars fields ");
        questionAnswer.put("ecole-tourisme", "the tourism schools in morocco are: ISIT tanger and in the tourism field in some OFPPTS like :Nador,tanger..");
        questionAnswer.put("ecole-militaire", "the military schools in morocco are: ARM, ERRSSM,ERN,ERA where you get to a physical exam too ");
        questionAnswer.put("conversation-error", "i'm sorry,can't understand your message.ask another question");
        questionAnswer.put("schools","we have more than x university in Morocco");

    }




    @PostMapping("/ask")
    public Answer Welcome(@RequestBody Message message) throws IOException {
        DoccatModel model = trainCategorizerModel();



        // Take chat inputs from console (user) in a loop.
        //Scanner scanner = new Scanner(System.in);



        // Get chat input from user.
        System.out.println("--> Me:");
        String userInput = message.getText();

        // Break users chat input into sentences using sentence detection.
        String[] sentences = breakSentences(userInput);


        System.out.println(mr.findAll().size());


        Random rd = new Random();


        mr.save(message);


        System.out.println(sr.findByName(message.getText()));

        //questionAnswer.put("ecole-economie", "Le seuil est "+ sr.findByName(message.getText()).get(0).getSeuil() );




        // questionAnswer.put("ecole-economie", "Le seuil est "+ sr.findByName(message.getText()).get(0).getSeuil() );





        //questionAnswer.put("schools","we have more than "+ sr.findAll().size()+"university in Morocco");
        System.out.println(sr.findAll().size());
        // questionAnswer.put("job", "We are lead company in software solution , we have more than "+sr.findAll().size()+"  you wanna apply ? ");

        String answer = "";
        boolean conversationComplete = false;

        // Loop through sentences.
        for (String sentence : sentences) {

            // Separate words from each sentence using tokenizer.
            String[] tokens = tokenizeSentence(sentence);

            // Tag separated words with POS tags to understand their gramatical structure.
            String[] posTags = detectPOSTags(tokens);

            // Lemmatize each word so that its easy to categorize.
            String[] lemmas = lemmatizeTokens(tokens, posTags);

            // Determine BEST category using lemmatized tokens used a mode that we trained
            // at start.
            String category = detectCategory(model, lemmas);

            // Get predefined answer from given category & add to answer.
            answer = answer + " " + questionAnswer.get(category);


        }

        // Print answer back to user. If conversation is marked as complete, then end
        // loop & program.
        System.out.println("-->RH Chat Bot: " + answer);

        Answer botAnswer = new Answer(rd.nextInt(), answer);
        ar.save(botAnswer);



        return botAnswer;

    }



    /**
     * Train categorizer model as per the category sample training data we created.
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static DoccatModel trainCategorizerModel() throws FileNotFoundException, IOException {
        // faq-categorizer.txt is a custom training data with categories as per our chat
        // requirements.
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("faq-categorizer.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0);

        // Train a model with classifications from above file.
        DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
        return model;
    }

    /**
     * Detect category using given token. Use categorizer feature of Apache OpenNLP.
     *
     * @param model
     * @param finalTokens
     * @return
     * @throws IOException
     */
    private static String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {

        // Initialize document categorizer tool
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

        // Get best possible category.
        double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
        String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
        System.out.println("Category: " + category);

        return category;

    }

    /**
     * Break data into sentences using sentence detection feature of Apache OpenNLP.
     *
     * @param data
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String[] breakSentences(String data) throws FileNotFoundException, IOException {
        // Better to read file once at start of program & store model in instance
        // variable. but keeping here for simplicity in understanding.
        try (InputStream modelIn = new FileInputStream("en-sent.bin")) {

            SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));

            String[] sentences = myCategorizer.sentDetect(data);
            System.out.println("Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")));

            return sentences;
        }
    }

    /**
     * Break sentence into words & punctuation marks using tokenizer feature of
     * Apache OpenNLP.
     *
     * @param sentence
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String[] tokenizeSentence(String sentence) throws FileNotFoundException, IOException {
        // Better to read file once at start of program & store model in instance
        // variable. but keeping here for simplicity in understanding.
        try (InputStream modelIn = new FileInputStream("en-token.bin")) {

            // Initialize tokenizer tool
            TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));

            // Tokenize sentence.
            String[] tokens = myCategorizer.tokenize(sentence);
            System.out.println("Tokenizer : " + Arrays.stream(tokens).collect(Collectors.joining(" | ")));

            return tokens;

        }
    }

    /**
     * Find part-of-speech or POS tags of all tokens using POS tagger feature of
     * Apache OpenNLP.
     *
     * @param tokens
     * @return
     * @throws IOException
     */
    private static String[] detectPOSTags(String[] tokens) throws IOException {
        // Better to read file once at start of program & store model in instance
        // variable. but keeping here for simplicity in understanding.
        try (InputStream modelIn = new FileInputStream("en-pos-maxent.bin")) {

            // Initialize POS tagger tool
            POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

            // Tag sentence.
            String[] posTokens = myCategorizer.tag(tokens);
            System.out.println("POS Tags : " + Arrays.stream(posTokens).collect(Collectors.joining(" | ")));

            return posTokens;

        }

    }

    /**
     * Find lemma of tokens using lemmatizer feature of Apache OpenNLP.
     *
     * @param tokens
     * @param posTags
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    private static String[] lemmatizeTokens(String[] tokens, String[] posTags)
            throws InvalidFormatException, IOException {
        // Better to read file once at start of program & store model in instance
        // variable. but keeping here for simplicity in understanding.
        try (InputStream modelIn = new FileInputStream("en-lemmatizer.bin")) {

            // Tag sentence.
            LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
            String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);
            System.out.println("Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")));

            return lemmaTokens;

        }
    }


}