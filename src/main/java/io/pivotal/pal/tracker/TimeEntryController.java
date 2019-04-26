package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;


    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntry){
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id){
        TimeEntry foundTimeEntity = timeEntryRepository.find(id);
        if(null==foundTimeEntity){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return new ResponseEntity<>(foundTimeEntity, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){
        actionCounter.increment();
        return new ResponseEntity<>(timeEntryRepository.list(),HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry timeEntry){
        TimeEntry updatedTimeEntry = timeEntryRepository.update(id, timeEntry);
        if(null==updatedTimeEntry){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return new ResponseEntity(updatedTimeEntry, HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id){
      /*  TimeEntry foundTimeEntry = timeEntryRepository.find(id);
        if(foundTimeEntry != null) {
            timeEntryRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/
        timeEntryRepository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
