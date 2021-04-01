package com.example.reactivepostgredemo.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapExample {
//    FlatMap : 변환 연산자
//    SwitchMap : 조합 연산자
//    ConcatMap : 계산 및 집합 연산자

    @AllArgsConstructor
    @Getter
    @Setter
    static class Man {
        private String name;
        private Integer age;

        // constructor, getter, setter 생략
    }

    public static void main(String[] args) {
        List<Man> ManList = Arrays.asList(
                new Man("Kimtaeng", 30),
                new Man("Madplay", 29));

        Set<String> names = ManList.stream()
                .map(Man::getName)
                .collect(Collectors.toSet());

        //단일 스트림 안의 요소를 원하는 특정 형태로 변환할 수 있습니다\
        //Person 이라는 클래스의 객체가 담긴 리스트에서 문자열인 name 필드만 Set 자료구조에 담은 후 출력
//        names.forEach(mapData -> System.out.println("map :::::" + mapData));


//        ===========================================================================


        String[][] namesArray = new String[][]{
                {"kim", "taeng"}, {"mad", "play"},
                {"kim", "mad"}, {"taeng", "play"}};

        Set<String> namesWithFlatMap = Arrays.stream(namesArray)
                .flatMap(innerArray -> Arrays.stream(innerArray))
                .filter(name -> name.length() > 3)
                .collect(Collectors.toSet());

//        flatMap 메서드는 스트림의 형태가 배열과 같을 때, 모든 원소를 단일 원소 스트림으로 반환할 수 있습니다
//        namesWithFlatMap.forEach(flatMapData -> System.out.println("flatmap :::" + flatMapData));


//        ================================================================================

//        map 메서드는 스트림의 스트림을 반환하는 반면에 flatMap 메서드는 스트림을 반환한다고 보면 됩니다.
//        특히 스트림의 형태가 배열인 경우 또는 입력된 값을 또 다시 스트림의 형태로 반환하고자 할 때는 flatMap이 유용합니다.
        String[][] namesArray2 = new String[][]{
                {"kim", "taeng"}, {"mad", "play"}};

// flatMap
        Arrays.stream(namesArray2)
                .flatMap(inner -> Arrays.stream(inner))
                .filter(name -> name.equals("taeng"))
                .forEach(System.out::println);

// map
        Arrays.stream(namesArray2)
                .map(inner -> Arrays.stream(inner))
                .forEach(n -> n.filter(name -> name.equals("taeng"))
                        .forEach(System.out::println));

    }

}
