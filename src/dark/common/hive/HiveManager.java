package dark.common.hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dark.common.prefab.Pos;

public class HiveManager
{

    protected static List<Hivemind> hives = new ArrayList<Hivemind>();
    protected static HashMap<String, List<Hivemind>> hivesets = new HashMap<String, List<Hivemind>>();
    public static final String NEUTRIAL = "NEUT";

    /** Register a network to a list */
    public static void registerHive(Hivemind mind)
    {
        if (!hives.contains(mind))
        {
            hives.add(mind);
            String name = mind.getID();
            List<Hivemind> list = new ArrayList<Hivemind>();
            list.add(mind);
            if (hivesets.containsKey(name) && hivesets.get(name) != null)
            {
                list.addAll(hivesets.get(name));
            }
            hivesets.put(name, list);
        }else
        {
            hives.get(mind).merger(mind);
        }
    }

    /** Removes a network from the list */
    public static void removeHive(Hivemind mind)
    {
        hives.remove(mind);
        changeHiveTag(mind, null);

    }

    public static void changeHiveTag(Hivemind mind, String tag)
    {
        if (hivesets.containsKey(mind.getID()))
        {
            List<Hivemind> list = hivesets.get(mind.getID());
            if (list == null)
            {
                hivesets.remove(mind.getID());
            }
            else
            {
                list.remove(mind);
                hivesets.put(mind.getID(), list);
            }
        }
        if (tag != null)
        {

        }
    }

    public static List<Hivemind> getHives()
    {
        return hives;
    }

    /** Gets the string ID the bot or Tile will use to ID itself as part of the hive */
    public static String getHiveID(Object obj)
    {
        Hivemind hive = null;
        double distance = Double.MAX_VALUE;
        Pos pos = null;
        World world = null;

        if (obj instanceof Entity)
        {
            pos = new Pos((Entity) obj);
            world = ((Entity) obj).worldObj;
        }
        else if (obj instanceof TileEntity)
        {
            pos = new Pos((TileEntity) obj);
            world = ((TileEntity) obj).worldObj;
        }

        if (pos != null && world != null)
        {
            for (Hivemind entry : getHives())
            {
                double distanceTo = entry.getLocation().getDistanceFrom(pos);
                if (distanceTo < distance)
                {
                    hive = entry;
                    distance = distanceTo;
                }
            }
            if (hive != null)
            {
                return hive.getID();
            }
        }

        return NEUTRIAL;
    }

    public void cleanup()
    {
        Iterator<Hivemind> it = HiveManager.getHives().iterator();
        while (it.hasNext())
        {
            Hivemind mind = it.next();
            if (mind.hiveBots.size() < 0 && mind.hiveTiles.size() < 0)
            {
                it.remove();
                changeHiveTag(mind, null);
            }
        }
    }

    public static Hivemind getHive(String hiveName)
    {
        return hivesets.get(hiveName) != null ? hivesets.get(hiveName).get(0) : null;
    }
}
