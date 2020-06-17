package com.peisia.klesa.data;
import com.peisia.klesa.MyApp;

import java.util.ArrayList;
import java.util.HashMap;
/***********************************************************************************************
 * 시공 이라는 뜻. Room 클래스를 포함함. 로딩 처리도 일단 이 안에서 처리.
 **********************************************************************************************/
public class Spatiotemporal {
    HashMap<Long, Room> mRooms;
    public HashMap<Long, Room> loadRooms(){
        mRooms = new HashMap<>();

        mRooms.put(1251110101001001001L, new Room("연습장 입구","연습장으로 들어가는 입구다. 나무로 만든 대문이 열려있고 특별한 건 보이지 않는다.", 1111L, 1251110101001001001L,
                createRoomDirection(new int[]{MyApp.DIRECTION_EAST}, new long[]{1251110101002001001L})
                ));

        mRooms.put(1251110101002001001L, new Room("연습장 남서쪽","초보자를 위한 연습장이다. 허수아비가 몇개 서있다.", 1211L, 1251110101002001001L,
                createRoomDirection(new int[]{MyApp.DIRECTION_WEST, MyApp.DIRECTION_NORTH}, new long[]{1251110101001001001L, 1251110101002002001L})
                ));

        mRooms.put(1251110101002002001L, new Room("연습장 북서쪽","초보자를 위한 연습장이다. 쓰레기가 나뒹굴고 있다..", 1221L, 1251110101002002001L,
                createRoomDirection(new int[]{MyApp.DIRECTION_SOUTH, MyApp.DIRECTION_EAST}, new long[]{1251110101002001001L, 1251110101002003001L})
                ));

        mRooms.put(1251110101002003001L, new Room("연습장 북동쪽","초보자를 위한 연습장이다. 바닥에 오래돼 보이는 핏자국이 묻어있다.", 1321L, 1251110101002003001L,
                createRoomDirection(new int[]{MyApp.DIRECTION_WEST, MyApp.DIRECTION_SOUTH}, new long[]{1251110101002002001L, 1251110101003001001L})
                ));

        mRooms.put(1251110101003001001L, new Room("연습장 남동쪽","초보자를 위한 연습장이다. 음산한 기운이 느껴진다.", 1311L, 1251110101003001001L,
                createRoomDirection(new int[]{MyApp.DIRECTION_NORTH}, new long[]{1251110101002003001L})
                ));

//        Room room = new Room("연습장 입구","연습장으로 들어가는 입구다. 나무로 만든 대문이 열려있고 특별한 건 보이지 않는다.", 1111L, 1251110101001001001L);
//        HashMap<Integer, Long> roomDirection = new HashMap<>();
//        roomDirection.put(MyApp.DIRECTION_EAST,1251110101002001001L);
//        room.setRoomConnections(roomDirection);
//        mRooms.put(1251110101001001001L, room);

//        mRooms.put(1111L, new Room("연습장 입구","연습장으로 들어가는 입구다. 나무로 만든 대문이 열려있고 특별한 건 보이지 않는다.", 1111L, 1251110101001001001L));
//        mRooms.put(1211L, new Room("연습장 남서쪽","초보자를 위한 연습장이다. 허수아비가 몇개 서있다.", 1211L, 1251110101002001001L));
//        mRooms.put(1221L, new Room("연습장 북서쪽","초보자를 위한 연습장이다. 쓰레기가 나뒹굴고 있다..", 1221L, 1251110101002002001L));
//        mRooms.put(1321L, new Room("연습장 북동쪽","초보자를 위한 연습장이다. 바닥에 오래돼 보이는 핏자국이 묻어있다.", 1321L, 1251110101002003001L));
//        mRooms.put(1311L, new Room("연습장 남동쪽","초보자를 위한 연습장이다. 음산한 기운이 느껴진다.", 1311L, 1251110101003001001L));
        return mRooms;
    }

    private HashMap<Integer, Long> createRoomDirection(int [] n, long [] l){
        if(n.length == l.length){
            HashMap<Integer, Long> roomDirections = new HashMap<>();
            for(int i=0;i<n.length;i++){
                roomDirections.put(n[i], l[i]);
            }
            return roomDirections;
        }
        return null;
    }
}
