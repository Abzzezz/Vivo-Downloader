package net.bplaced.abzzezz.vivodownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.concurrent.TimeUnit;

public class MainClass {

    public static final MainClass INSTANCE = new MainClass();

    public final Decoder decoder;

    public MainClass() {
        this.decoder = new Decoder();
    }

    public static void main(final String[] args) {
        MainClass.INSTANCE.start(args);
    }

    /**
     * Takes in parameters (url and output)
     *
     * @param args arguments
     */
    private void start(final String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments specified");
            return;
        }
        final File output = new File(args[1]);
        final String fileName = args[2];

        try {
            final String decodedUrl = decoder.decodeVivo(args[0]);
            final long startTime = System.currentTimeMillis();

            final FileOutputStream fileOutputStream = new FileOutputStream(new File(output, fileName));
            fileOutputStream.getChannel().transferFrom(Channels.newChannel(new URL(decodedUrl).openStream()), 0, Long.MAX_VALUE);
            fileOutputStream.close();

            final long finalTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);

            System.out.printf("Done downloading file %s from %s. This took %d seconds.", fileName, decodedUrl, finalTime);
        } catch (IOException e) {
            System.out.println("Could not decode vivo.sx url");
            e.printStackTrace();
        }
    }
}
