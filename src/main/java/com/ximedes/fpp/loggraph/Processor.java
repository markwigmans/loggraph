package com.ximedes.fpp.loggraph;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mawi on 30/03/2017.
 */
public class Processor {

    final BufferedReader in;
    final NullableBufferedWriter timerOut;
    final NullableBufferedWriter hibernateOut;
    final List<String> timerKeywords = Lists.newArrayList("timestamp", "count", "min", "max", "mean", "stddev", "median", "p75", "p95", "p98", "p99", "p999", "mean_rate", "m1", "m5", "m15");

    public Processor(final InputStream input, final OutputStream tOut, final OutputStream hOut) {
        in = new BufferedReader(new InputStreamReader(input));
        timerOut = new NullableBufferedWriter(tOut);
        hibernateOut = new NullableBufferedWriter(hOut);
    }

    void process() throws IOException {
        // write headers
        timerOut.write(timerKeywords.stream().collect(Collectors.joining(",")));
        timerOut.newLine();
        hibernateOut.write(Lists.newArrayList("time", "rows", "query").stream().collect(Collectors.joining(",")));
        hibernateOut.newLine();

        in.lines().forEach(line -> processLine(line));

        timerOut.flush();
        hibernateOut.flush();
    }

    private void processLine(final String line) {
        try {
            if (line.indexOf("net.mcfpp.metrics") > 0) {
                final String processed = processTimerMetrics(line).stream()
                        .map(StringEscapeUtils::escapeCsv)
                        .collect(Collectors.joining(","));
                if (StringUtils.isNoneBlank(processed)) {
                    timerOut.write(processed);
                    timerOut.newLine();
                }
            } else {
                if (line.indexOf("org.hibernate.stat.Statistics") > 0) {
                    final String processed = processHibernateMetrics(line).stream()
                            .map(StringEscapeUtils::escapeCsv)
                            .collect(Collectors.joining(","));
                    if (StringUtils.isNoneBlank(processed)) {
                        hibernateOut.write(processed);
                        hibernateOut.newLine();
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    List<String> processTimerMetrics(final String line) {
        if (line.indexOf("type=TIMER") > 0) {
            final List<String> output = Lists.newArrayList();
            output.add(findTimestamp(line));

            final Splitter splitter = Splitter.on(',').omitEmptyStrings().trimResults();
            for (String element : splitter.split(line)) {
                final String[] split = element.split("=");
                if (timerKeywords.contains(split[0])) {
                    output.add(split[1]);
                }
            }
            return output;
        } else {
            return Collections.emptyList();
        }
    }

    String findTimestamp(final String line) {
        final Pattern p = Pattern.compile(".*(\\d{2}:\\d{2}:\\d{2}[.,]\\d{3}).*");
        Matcher m = p.matcher(line);
        if (m.matches()) {
            return m.group(1);
        }
        return "";
    }


    List<String> processHibernateMetrics(final String line) {
        final int index = line.lastIndexOf("time:");
        final List<String> output = Lists.newArrayList();
        final Splitter splitter = Splitter.on(',').omitEmptyStrings().trimResults();
        for (String element : splitter.split(line.substring(index))) {
            final String[] split = element.split(":");
            final String key = split[0].trim();
            final String value = split[1].trim();
            if (key.equals("time")) {
                output.add(StringUtils.left(value, value.length() - 2));
            } else {
                output.add(value);
            }
        }
        output.add(line.substring(line.indexOf("HQL:"), index));
        return output;
    }
}

class NullableBufferedWriter {

    final BufferedWriter out;

    NullableBufferedWriter(final OutputStream output) {
        if (output != null) {
            out = new BufferedWriter(new OutputStreamWriter(output));
        } else {
            out = null;
        }
    }

    void write(String text) throws IOException {
        if (out != null) {
            out.write(text);
        }
    }

    void newLine() throws IOException {
        if (out != null) {
            out.newLine();
        }
    }

    void flush() throws IOException {
        if (out != null) {
            out.flush();
        }
    }
}
