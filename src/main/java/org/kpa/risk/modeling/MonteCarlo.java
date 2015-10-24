package org.kpa.risk.modeling;

import org.kpa.risk.protfolio.PortfolioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by krucpav on 27.09.15.
 */
public class MonteCarlo {
    final static Logger logger = LoggerFactory.getLogger(MonteCarlo.class);
    private final StrategyConfig config;
    private final int numberOfStrategies;
    private final int iterationCount;

    public MonteCarlo(StrategyConfig config, int numberOfStrategies, int iterationCount) {
        this.config = config;
        this.numberOfStrategies = numberOfStrategies;
        this.iterationCount = iterationCount;
    }

    public Statistics start() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<PortfolioManager>> tasks = new ArrayList<>();
        for (int i = 0; i < iterationCount; i++) {
            tasks.add(executor.submit(() -> iterate()));
            logger.info("Enqueued task {} of {}", i, iterationCount);
        }
        executor.shutdown();
        int i = 0;
        Statistics statistics = new Statistics();
        for (Future<PortfolioManager> future : tasks) {
            try {
                PortfolioManager stat = future.get();
                statistics.addResult(stat.getTrackList().getPl(), stat.getTrackList().getDd());
                logger.info("Complete task {} of {}", ++i, tasks.size());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return statistics;
    }

    private PortfolioManager iterate() {
//        final PortfolioManager manager = new PortfolioManager(DamperedRiskSource.factory(.5));
        final PortfolioManager manager = new PortfolioManager(PlainRiskSource.factory());
        for (int i = 0; i < numberOfStrategies; i++) {
            // mean pl is not more then predicted
            Double meanPl = ThreadLocalRandom.current().nextDouble(-config.getTargetPnl() * 2, config.getTargetPnl() * 2);
            // mean DD can be more then predicted for up to two times
            Double meadDD = ThreadLocalRandom.current().nextDouble(config.getMaxDrawDown() * 2);
            manager.addStrategy(new RandomStrategy("" + i, meanPl, meadDD, config.getPeriodCount()), config);
        }
        for (int i = 0; i < iterationCount; i++) {
            manager.nextPeriod();
        }
        return manager;
    }


    public static void main(String[] args) {
        try {
            double pl = Double.parseDouble(args[0]);
            double dd = Double.parseDouble(args[1]);
            int periods = Integer.parseInt(args[2]);
            int strategiesNumber = Integer.parseInt(args[3]);
            int iterationsNumber = Integer.parseInt(args[4]);

            StrategyConfig config = StrategyConfig.pnl(pl).dd(dd).count(periods).config();
            logger.info("Running MonteCarlo with params:\nStrategy config={},\nstrategiesNumber={}, iterationsNumber={}",
                    config.toString(), strategiesNumber, iterationsNumber);
            MonteCarlo monteCarlo = new MonteCarlo(config, strategiesNumber, iterationsNumber);
            Statistics statistics = monteCarlo.start();
            logger.info("Result {}", statistics);
            statistics.showChart();
            Files.write(Paths.get("output.txt"), statistics.dumpString(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("Usage");
            System.out.println("\tjava jar");
        }
    }
}
