package extra.jtable;

public class EventEx {
    private Integer _time;
    private Integer _priority;

    public EventEx(Integer time, Integer prioridad){
        _priority = prioridad;
        _time = time;
    }
    public int getTime(){
        return _time;
    }
    public int getPriority(){
        return _priority;
    }
}
