package com.ximedes.fpp.loggraph;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by mawi on 30/03/2017.
 */
@Component
public class Runner implements CommandLineRunner {

    final static String Input = "INPUT";
    final static String Timer = "TIMER";
    final static String Hibernate = "HIBERNATE";

    @Override
    public void run(String... args) throws Exception {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("prog").description("Process logback metrics file");
        parser.addArgument("-i", "--input").dest(Input).type(FileInputStream.class).setDefault(System.in);
        parser.addArgument("-ts", "--timer").nargs("?").dest(Timer).type(FileOutputStream.class);
        parser.addArgument("-hs", "--hibernate").nargs("?").dest(Hibernate).type(FileOutputStream.class);

        try {
            final Namespace res = parser.parseArgs(args);
            Processor processor = new Processor(res.get(Input), res.get(Timer), res.get(Hibernate));
            processor.process();
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }
}
