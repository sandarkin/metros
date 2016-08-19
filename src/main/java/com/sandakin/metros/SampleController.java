package com.sandakin.metros;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

@Controller
public class SampleController {

  @Autowired
  private GaugeService gauges;

  @Autowired
  private MetricRegistry registry;

  @GetMapping("/")
  @ResponseBody
  public Map<String, String> hello() {
    this.gauges.submit("timer.test", Math.random() * 1000 + 1000);
    return Collections.singletonMap("message", "Tralala");
  }

  @GetMapping("/reg")
  @ResponseBody
  public Timer getRegs() {
    return this.registry.getTimers().get("timer.test");
  }

  @PostConstruct
  private void init() {
    ConsoleReporter reporter = ConsoleReporter.forRegistry(this.registry)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
    reporter.start(1, TimeUnit.SECONDS);
  }


}
