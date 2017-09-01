package com.ximedes.fpp.loggraph;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * Created by mawi on 30/03/2017.
 */
public class ProcessorTest {
    @Test
    public void processTimerOut1() throws Exception {
        final InputStream stream = new ClassPathResource("timer.out").getInputStream();
        final Processor processor = new Processor(stream, System.out, null);
        processor.process();
    }

    @Test
    public void processTimerOut2() throws Exception {
        final InputStream stream = new ClassPathResource("perf.log").getInputStream();
        final Processor processor = new Processor(stream, System.out, null);
        processor.process();
    }

    @Test
    public void processPerfLog() throws Exception {
        final InputStream stream = new ClassPathResource("perf.log").getInputStream();
        final Processor processor = new Processor(stream, null, System.out);
        processor.process();
    }

    @Test
    public void findTimestamp1() throws Exception {
        final Processor processor = new Processor(new ClassPathResource("timer.out").getInputStream(), null, null);
        Assert.assertEquals("2017-06-26 15:04:05.150", processor.findTimestamp("2017-06-26 15:04:05,150 [rter-1-thread-1] E:           T:                     R: S:      U:           INFO    net.mcfpp.metrics - type=TIMER, name=net.mcfpp.ctn.server.http.HTTP_CTNServlet.CTN requests, count=4, min=146.80763199999998, max=869.43518, mean=186.29656501935514, stddev=36.73430727427362, median=185.081582, p75=185.081582, p95=186.781559, p98=186.781559, p99=186.781559, p999=869.43518, mean_rate=1.4987987556953025E-5, m1=2.964393875E-314, m5=1.4821969375E-313, m15=3.3904063983955295E-130, rate_unit=events/second, duration_unit=milliseconds"));
    }

    @Test
    public void findTimestamp2() throws Exception {
        final Processor processor = new Processor(new ClassPathResource("timer.out").getInputStream(), null, null);
        Assert.assertEquals("00:01:41.243", processor.findTimestamp("server.2017-03-24.0.log:00:01:41.243 [rter-1-thread-1] E:           T:                     R: S:      U:           INFO    net.mcfpp.metrics - type=TIMER, name=net.mcfpp.ctn.server.http.HTTP_CTNServlet.CTN requests, count=51, min=50.563967, max=1016.486954, mean=510.9611211845533, stddev=9.80013975890173, median=510.291089, p75=523.7463359999999, p95=523.7463359999999, p98=523.7463359999999, p99=523.7463359999999, p999=523.7463359999999, mean_rate=0.008296320182867798, m1=2.803077943726639E-6, m5=0.0015782542075735192, m15=0.004212108570143394, rate_unit=events/second, duration_unit=milliseconds"));
    }

    @Test
    public void findTimestamp3() throws Exception {
        final Processor processor = new Processor(new ClassPathResource("timer.out").getInputStream(), null, null);
        Assert.assertEquals("2017-06-26 15:04:05.150", processor.findTimestamp("server.2017-03-24.0.log:2017-06-26 15:04:05,150 [rter-1-thread-1] E:           T:                     R: S:      U:           INFO    net.mcfpp.metrics - type=TIMER, name=net.mcfpp.ctn.server.http.HTTP_CTNServlet.CTN requests, count=4, min=146.80763199999998, max=869.43518, mean=186.29656501935514, stddev=36.73430727427362, median=185.081582, p75=185.081582, p95=186.781559, p98=186.781559, p99=186.781559, p999=869.43518, mean_rate=1.4987987556953025E-5, m1=2.964393875E-314, m5=1.4821969375E-313, m15=3.3904063983955295E-130, rate_unit=events/second, duration_unit=milliseconds"));
    }
}