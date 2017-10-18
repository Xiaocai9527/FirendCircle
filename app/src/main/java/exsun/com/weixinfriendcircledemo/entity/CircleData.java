package exsun.com.weixinfriendcircledemo.entity;

import java.util.List;

/**
 * @author xiaokun
 * @date 2017/10/18
 */

public class CircleData
{
    public List<CircleItem> list;

    public List<CircleItem> getList()
    {
        return list;
    }

    public void setList(List<CircleItem> list)
    {
        this.list = list;
    }

    @Override
    public String toString()
    {
        return "CircleData{" +
                "list=" + list +
                '}';
    }
}
