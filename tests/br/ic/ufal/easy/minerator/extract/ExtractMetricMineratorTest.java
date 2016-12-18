package br.ic.ufal.easy.minerator.extract;

import br.ic.ufal.easy.historic.minerator.extract.method.ExtractMetricMinerator;
import org.junit.Test;

/**
 * Created by Ana Carla on 18/12/2016.
 */
public class ExtractMetricMineratorTest {

    @Test
    public void testarMinerateExtractMethod(){
        new ExtractMetricMinerator().minerateExtractMethod("clipOcr_extract_method.csv");
    }
}
