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
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;




public class WeakArrayList <T> implements Iterable<T>
{

    private CopyOnWriteArrayList<WeakReference<T>> mList;


    public  WeakArrayList()
    {
        init();
    }

    public WeakArrayList(CopyOnWriteArrayList<WeakReference<T>> mList)
    {
        this.mList = mList;
    }



    //----------------------------------------------------------------------------------------------
    // init
    //----------------------------------------------------------------------------------------------
    private void init()
    {
        mList = new CopyOnWriteArrayList<WeakReference<T>>();
    }



    //----------------------------------------------------------------------------------------------
    // add
    //----------------------------------------------------------------------------------------------
    public synchronized void add(T item)
    {
        mList.add(new WeakReference<T>(item));
    }


    //----------------------------------------------------------------------------------------------
    // addUnique
    //----------------------------------------------------------------------------------------------
    public synchronized void addUnique(T item)
    {
        if(!contains(item)) mList.add(new WeakReference<T>(item));
    }




    //----------------------------------------------------------------------------------------------
    // remove
    //----------------------------------------------------------------------------------------------
    public synchronized WeakReference<T> remove(int index)
    {
        return mList.remove(index);
    }


    //----------------------------------------------------------------------------------------------
    // remove
    //----------------------------------------------------------------------------------------------
    public synchronized boolean remove(T item)
    {
        for(WeakReference<T> weakItem : mList)
        {
            T strongItem = weakItem.get();
            if(strongItem == item)
            {
                mList.remove(weakItem);
                return true;
            }
        }

        return false;
    }


    //----------------------------------------------------------------------------------------------
    // contains
    //----------------------------------------------------------------------------------------------
    public synchronized boolean contains(T item)
    {
        for(WeakReference<T> weakItem : mList)
        {
            if(weakItem.get() == item) return true;
        }
        return false;
    }


    //----------------------------------------------------------------------------------------------
    // clear
    //----------------------------------------------------------------------------------------------
    public synchronized void clear()
    {
        mList.clear();
    }


    //----------------------------------------------------------------------------------------------
    // size
    //----------------------------------------------------------------------------------------------
    public synchronized int size()
    {
        return mList.size();
    }



    //----------------------------------------------------------------------------------------------
    // get
    //----------------------------------------------------------------------------------------------
    public synchronized T get(int index)
    {
        return mList.get(index).get();
    }



    //----------------------------------------------------------------------------------------------
    // Iterator<T>
    //----------------------------------------------------------------------------------------------
    @Override
    public Iterator<T> iterator()
    {
        Iterator<T> it = new Iterator<T>()
        {
            int pos =-1;
            T nextItem = null;

            @Override
            public boolean hasNext()
            {
               nextItem = null;
               while(pos < mList.size()-1)
               {
                   nextItem =  mList.get(pos+1).get() ;
                   if(nextItem == null)  mList.remove(pos+1);
                   else return true;
               }

               return false;
            }

            @Override
            public T next()
            {
                pos++;
                T item =  mList.get(pos).get();
                nextItem = null;
                return item;
            }

            @Override
            public void remove()
            {

            }
        };
        return it;
    }
}
