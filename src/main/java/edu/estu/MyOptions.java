package edu.estu;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.util.List;

public class MyOptions {
    @Option(name="-r",usage="reverses order")
    boolean reverse;

    @Option(name="-u",usage="unique")
    boolean unique;

    @Option(name = "-task",required = true)
    String task;

    @Option(name= "-topN", usage = " ")
    int num = 5;

    @Option(name = "-start")
    String startsWith;

    //@Argument List<String> argument = new ArrayList<String>();


    @Argument(required = true,handler = StringArrayOptionHandler.class, usage = "name of the file that you want to sort")
    List<String> filenames;



}
