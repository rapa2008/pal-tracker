package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long, TimeEntry> mapTimeEntry = new HashMap<>();
    Long currentid = 1L;
    @Override
    public TimeEntry create(TimeEntry timeEntry){
        Long id = currentid++;
        TimeEntry newTimeEntry = new TimeEntry(
                id,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );
        System.out.println("Created " + newTimeEntry.toString());
        mapTimeEntry.put(newTimeEntry.getId(), newTimeEntry);
        return newTimeEntry;
    }

    @Override
    public TimeEntry find(long id){
        if(mapTimeEntry.containsKey(id)){
            return mapTimeEntry.get(id);
        }
        return null;
    }

    @Override
    public List<TimeEntry> list(){
        List<TimeEntry> listTimeEntry = new ArrayList<>(mapTimeEntry.values());
        return listTimeEntry;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry){
        if (find(id) == null) return null;

        TimeEntry updatedEntry = new TimeEntry(
                id,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );

        mapTimeEntry.replace(id, updatedEntry);
        return updatedEntry;
    }

    @Override
    public void delete(long id){
        if(mapTimeEntry.containsKey(id)){
            mapTimeEntry.remove(id);
        }
    }
}
