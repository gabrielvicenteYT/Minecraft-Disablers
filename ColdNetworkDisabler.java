public class ColdNetworkDisabler extends Module {
    public OnlyMCDisabler() {
        super("ColdNetworkDisabler", "Disables Cold Networks's Anticheat", Catagory.MISC); // They use a weird config of verus 
    }
    ArrayList<Packet> transactions = new ArrayList<Packet>();
    int currentTransaction = 0;
    
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        
        if(packet instanceof C0FPacketConfirmTransaction) {
            transactions.add(packet);
            event.setCanceled(true);
        }
        
        if(packet instanceof C00PacketKeepAlive) {
            ((C00PacketKeepAlive)packet).key -= RandomUtils.random(1, 2147483647);
        }
        
        if(packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer)packet;
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput());
            if(mc.thePlayer.ticksExisted % 15 == 0) {
                c03.y += RandomUtils.randDouble(100, 1000);
            }
        }
    }
    
    public void onEnable() {
       mc.thePlayer.ticksExisted = 0;
    }
    
    public void onDisable() {
       transactions.clear();
       currentTransaction = 0;
    }
    
    public void onWorldChange() {
        if(!this.getState()) return;
        transactions.clear();
        currentTransaction = 0;
    }
    
    public void onUpdate() {
        if(!this.getState()) return;
        mc.timer.timerSpeed = 0.4F;
        if(mc.thePlayer.ticksExisted % 25 == 0 && transactions.size() > currentTransaction) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(transactions.get(++currentTransaction)));
        }
    }
}