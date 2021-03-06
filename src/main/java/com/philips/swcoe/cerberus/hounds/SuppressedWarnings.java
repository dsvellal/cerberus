/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.hounds;

import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_CMD_LINE_OPTION_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE;
import static com.philips.swcoe.cerberus.constants.DescriptionConstants.SUPPRESSED_WARNINGS_DETECTOR_DESCRIPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.FILES_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.LANGUAGE_OPTION;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.RESULTS_OF_SWD;
import static com.philips.swcoe.cerberus.constants.ProgramConstants.SUPPRESSED_WARNINGS_DETECTOR;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.Callable;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.MapUtils;

import com.philips.swcoe.cerberus.cerebellum.swd.BaseSuppressedWarningsDetector;
import com.philips.swcoe.cerberus.cerebellum.swd.SuppressedWarningDetectors;
import com.philips.swcoe.cerberus.cerebellum.swd.SuppressedWarningsDetectorFactory;
import com.philips.swcoe.cerberus.cerebellum.tokenizer.DirectoryTokenizer;

import picocli.CommandLine;

@CommandLine.Command(name = SUPPRESSED_WARNINGS_DETECTOR, description = SUPPRESSED_WARNINGS_DETECTOR_DESCRIPTION)
public class SuppressedWarnings extends BaseCommand implements Callable<Integer> {

    @NotNull(message = FILES_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = FILES_OPTION, description = FILES_CMD_LINE_OPTION_DESCRIPTION)
    private String pathToSource;

    @NotNull(message = LANGUAGE_OPTION_NOT_NULL_ARGUMENT_MESSAGE)
    @CommandLine.Option(names = LANGUAGE_OPTION, description = LANGUAGE_CMD_LINE_OPTION_DESCRIPTION)
    private String languageOfSource;

    DirectoryTokenizer tokenizer;

    public SuppressedWarnings() {
        tokenizer = new DirectoryTokenizer();
    }

    @Override
    public Integer call() throws Exception {
        this.validate();
        tokenizer.tokenize(new File(pathToSource), languageOfSource);
        BaseSuppressedWarningsDetector aswd = SuppressedWarningsDetectorFactory.newInstance(SuppressedWarningDetectors.valueOf(languageOfSource));
        aswd.detect(tokenizer);
        MapUtils.verbosePrint(new PrintStream(System.out), RESULTS_OF_SWD, aswd.getSuppressedWarnings());
        return 0;
    }

}
