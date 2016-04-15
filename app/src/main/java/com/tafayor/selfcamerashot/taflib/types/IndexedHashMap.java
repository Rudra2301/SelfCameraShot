/*
 * Copyright (C) 2015 Ouadban Youssef(tafayor.dev@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.tafayor.selfcamerashot.taflib.types;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;



public class IndexedHashMap<K,V> extends LinkedHashMap<K,V>
{


    private ArrayList<K> mTags;


    public IndexedHashMap()
    {
        init();
    }

    public IndexedHashMap(ArrayList<K> tags)
    {
        this.mTags = tags;
    }



    //----------------------------------------------------------------------------------------------
    // init
    //----------------------------------------------------------------------------------------------
    private void init()
    {
        mTags = new ArrayList<K>();
    }


    //----------------------------------------------------------------------------------------------
    // put
    //----------------------------------------------------------------------------------------------
    @Override
    public V put(K key, V value)
    {
        if(!containsKey(key)) mTags.add(key);
        return super.put(key, value);
    }


    //----------------------------------------------------------------------------------------------
    // remove
    //----------------------------------------------------------------------------------------------
    @Override
    public V remove(Object key)
    {
        mTags.remove(key);
        return super.remove(key);
    }


    //----------------------------------------------------------------------------------------------
    // remove
    //----------------------------------------------------------------------------------------------
    public V remove(int index )
    {
        V ret =  super.remove(mTags.get(index));
        mTags.remove(index);
        return ret;
    }


    //----------------------------------------------------------------------------------------------
    // indexOf
    //----------------------------------------------------------------------------------------------
    public int indexOf(Object object)
    {
        int index = -1;
        for(int i =0; i<size();i++)
        {
            if(get(i) == object){index = i; break;}
        }
        return index;
    }


    //----------------------------------------------------------------------------------------------
    // get
    //----------------------------------------------------------------------------------------------
    public V get(int index)
    {
        return super.get(mTags.get(index));
    }


    //----------------------------------------------------------------------------------------------
    // getByKey
    //----------------------------------------------------------------------------------------------
    public V getByKey(K key)
    {
        return super.get(key);
    }


    //----------------------------------------------------------------------------------------------
    // getByIndex
    //----------------------------------------------------------------------------------------------
    public V getByIndex(int index)
    {
        return super.get(mTags.get(index));
    }


    //----------------------------------------------------------------------------------------------
    // getKey
    //----------------------------------------------------------------------------------------------
    public K getKey(int index)
    {
        return mTags.get(index);
    }

    //----------------------------------------------------------------------------------------------
    // getKeyIndex
    //----------------------------------------------------------------------------------------------
    public int getKeyIndex(K key)
    {
        return mTags.indexOf(key);
    }

}
