package com.tongji;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * 测试布隆过滤器(可用于redis缓存穿透)
 * @author tongji4m3
 */

public class TestBloomFilter {
    private static int total = 1000000;
    /*
    static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, BloomFilter.Strategy strategy)
    funnel：数据类型(⼀般是调⽤Funnels⼯具类中的)
    expectedInsertions：期望插⼊的值的个数
    fpp 错误率(默认值为0.03)
    strategy 哈希算法

    numBits，表示存⼀百万个int类型数字，需要的位数为7298440，700多万位。理论上存⼀百万个
    数，⼀个int是4字节32位，需要481000000=3200万位。如果使⽤HashMap去存，按HashMap50%的
    存储效率，需要6400万位。可以看出BloomFilter的存储空间很⼩，只有HashMap的1/10左右

    错误率越⼤，所需空间和时间越⼩，错误率越⼩，所需空间和时间越⼤
     */
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), total);

    public static void main(String[] args) {
        //初始化total条数据到过滤器里
        for (int i = 0; i < total; i++) {
            bloomFilter.put(i);
        }
        //匹配已经再过滤器中的值,看是否有匹配不上的
        //没有输出,说明只要放进去的,都能匹配上
        for (int i = 0; i < total; i++) {
            if (!bloomFilter.mightContain(i)) {
                System.out.println("有坏人逃脱啦~");
            }
        }

        //匹配不在过滤器中的10000个值,有多少匹配出来
        int count = 0;
        for (int i = total; i < total + 10000; i++) {
            if (bloomFilter.mightContain(i)) {
                ++count;
            }
        }
        //误伤数量: 320 错误率是0.03左右
        System.out.println("误伤数量: "+count);
    }
}
