package com.ximedes.fpp.loggraph;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * Created by mawi on 30/03/2017.
 */
public class ProcessorTest {
    @Test
    public void processTimerOut() throws Exception {
        final InputStream stream = new ClassPathResource("timer.out").getInputStream();
        final Processor processor = new Processor(stream, System.out, null);
        processor.process();
    }


    @Test
    public void processPerfLog() throws Exception {
        final InputStream stream = new ClassPathResource("perf.log").getInputStream();
        final Processor processor = new Processor(stream, null, System.out);
        processor.process();
    }
}