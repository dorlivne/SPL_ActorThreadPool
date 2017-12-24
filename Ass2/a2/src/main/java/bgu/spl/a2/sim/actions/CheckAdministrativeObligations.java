package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;

import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CheckAdministrativeObligations extends Action {
    private Warehouse _Warehouse;
    private List<String> _StudentsId;
    private List<String> _Condition;
    private String _CompId;

    public CheckAdministrativeObligations(List<String> StudentsId,List<String> Conditions
                                           ,String CompId,Warehouse warehouse){
        this._Warehouse = warehouse;
        this._Condition = Conditions;
        this._StudentsId = StudentsId;
        this._CompId = CompId;
        this.setActionName("Check Administrative Obligations");

    }
    @Override
    protected void start() {
        Promise<Computer> computer = _Warehouse.GetComputer(_CompId);
        computer.subscribe(()-> {//once we got a computer we can proceed
                Computer comp = computer.get();
                List<Action<Boolean>> actions = new ArrayList<>();
                for (String s : _StudentsId) {
                    Action<Boolean> Check = new CheckAdministrativeObligationsConfirmation(_Condition,comp);
                    actions.add(Check);
                    sendMessage(Check,s,new StudentPrivateState());//this submit the action to the pool should be enqueue in the students actor rqueue
                }
               then(actions, ()-> {
                   comp.Release();
                   this.ActorState.addRecord(getActionName());
                   for (String pref : _StudentsId) {
                       Boolean result = actions.get(_StudentsId.indexOf(pref)).getResult().get();
                       if (result == true) {
                           complete(true);
                       }else{
                           complete(false);
                       }
                   }
                     });
        });
    }
}
