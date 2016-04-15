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

import android.graphics.Point;
import android.graphics.PointF;


public class DimensionF
{
    public float width;
    public float height;

    public DimensionF()
    {
        width = 0;
        height = 0;
    }

    public DimensionF(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public DimensionF(DimensionF other)
    {
        this.width = other.width;
        this.height = other.height;
    }

    public DimensionF(PointF point)
    {
        width = point.x;
        height = point.y;
    }

    public boolean equals(DimensionF other)
    {
        return ((this.width == other.width) && (this.height == other.height));
    }

    public void importFrom(PointF point)
    {
        width = point.x;
        height = point.y;
    }

    public PointF toPoint()
    {
        return new PointF(width, height);
    }
}
