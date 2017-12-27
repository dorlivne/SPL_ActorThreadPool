package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;

import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CheckAdministrativeObligations extends Action {
    private Warehouse _Warehouse;
    private List<String> _StudentsId;
    private List<String> _Condition;
    private String _CompId;
    private AtomicBoolean _BeenHere = new AtomicBoolean();

    public CheckAdministrativeObligations(List<String> StudentsId,List<String> Conditions
                                           ,String CompId,Warehouse warehouse){
        this._Warehouse = warehouse;
        this._Condition = Conditions;
        this._StudentsId = StudentsId;
        this._CompId = CompId;
        this.setActionName("Administrative Check");
        this._BeenHere.set(false);

    }

    /**
     * first we claim a computer then we need to rerun the action so this is why we submit it again to the pool and also
     * manually defining the call back to call once the action is picked up again
     * after that we assign action to be preformed to the students with our computer(it still our computer)
     * once the students are done getting checked we use the then method to complete the action
     */
    @Override
    protected void start() {
        if(_BeenHere.compareAndSet(false,true)) {
            this.ActorState.addRecord(getActionName());
        }
        Promise<Computer> computer = _Warehouse.GetComputer(_CompId);
        computer.subscribe(()-> {//once we got a computer we can proceed
                Computer comp = computer.get();
                List<Action<Boolean>> actions = new ArrayList<>();
                for (String s : _StudentsId) {
                    Action<Boolean> Check = new CheckAdministrativeObligationsConfirmation(_Condition, comp);
                    actions.add(Check);
                    sendMessage(Check, s, new StudentPrivateState());//this submit the action to the pool should be enqueue in the students actor rqueue
                }
            this.Tag = ()-> {
                then(actions, () -> {
                        comp.Release();
                        for (String pref : _StudentsId) {
                            Boolean result = actions.get(_StudentsId.indexOf(pref)).getResult().get();
                            if (result == true) {
                                complete(true);
                            } else {
                                complete(false);
                            }
                        }
                });//then scope
             };//Tag scope
            this.Contiunation = true;
            this.sendMessage(this,this.ActorId,ActorState);//submit the continuation to the pool
        });//subscribe scope

    }
}
