package com.mayj.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
class MayjProjectApplicationTests {

    private Logger logger = LoggerFactory.getLogger(SpringBootTest.class);
    @Test
    void contextLoads() {
        //int a= 0xffffffff;

        //************************************************************


        //************************************************************
        List<Integer> nums = new ArrayList<>();
        nums.add(1);
        nums.add(657567);
        qishuiping(nums);
        //************************************************************
        //int[][] matrix = {
        //        {1,   4,  7, 11, 15},
        //        {2,   5,  8, 12, 19},
        //        {3,   6,  9, 16, 22},
        //        {10, 13, 14, 17, 24},
        //        {18, 21, 23, 26, 30}};
        //boolean rs = findNumberIn2DArray(matrix, 5);
        //logger.info("findNumberIn2DArray:{}", rs);
        //************************************************************
        //int[] nums = {2,3,1,0,2,5,3};
        //findRepeatNumber(nums);

    }

    /**
     * 剑指 Offer 03. 数组中重复的数字
     * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。
     * 数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请
     * 找出数组中任意一个重复的数字。
     *
     * @param nums
     * @return
     */
    public int findRepeatNumber(int[] nums) {
        if (Objects.equals(nums, null)) {
            return 0;
        }
        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (!map.containsKey(nums[i])) {
                map.put(nums[i], false);
            } else {
                logger.info("num:{}",nums[i]);
                return nums[i];
            }
        }
        return -1;
        //*************更优解
        //Set<Integer> set = new HashSet<Integer>();
        //        int repeat = -1;
        //        for (int num : nums) {
        //            if (!set.add(num)) {
        //                repeat = num;
        //                break;
        //            }
        //        }
        //        return repeat;
    }

    /**
     * 剑指 Offer 04. 二维数组中的查找
     * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
     * 请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * @param matrix
     * @param target
     * @return
     */
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (null == matrix || matrix.length - 1 < 0 || matrix[0].length - 1 < 0) return false;
        int row = matrix.length-1;
        int a = matrix[0].length-1;
        int col = 0;
        while (true){
            int value = matrix[row][col];
            if (target == value) return true;
            if (target > value) col++;
            if (target < value) row--;
            if (row < 0 || col > a) break;
        }
        return false;
        //*************更优解
        //int i = matrix.length - 1, j = 0;
        //        while(i >= 0 && j < matrix[0].length)
        //        {
        //            if(matrix[i][j] > target) i--;
        //            else if(matrix[i][j] < target) j++;
        //            else return true;
        //        }
        //        return false;
    }

    public void qishuiping(List<Integer> nums) {
        for (int num :
                nums) {
            if (num == 1){
                System.out.println(0);
                continue;
            }
            if (num == 2 || num == 3) {
                System.out.println(1);
                continue;
            }
            int count = 0;
            while (num > 1) {
                count++;
                num -= 2;
            }
            System.out.println(count);
        }
    }

    public void rand() {
        List<Integer> nums = new ArrayList<>();
        List<Integer> nums1 = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        Scanner in=new Scanner(System.in);
        int num = in.nextInt();
        for (int i = 0; i < num; i++) {
            nums.add(in.nextInt());
        }
        for (int n :
                nums) {
            if (!set.add(n)) continue;
            nums1.add(n);
        }
        Collections.sort(nums1);
        for (int n :
                nums1) {
            System.out.println(n);
        }
    }

    /**
     * 十六进制转十进制
     */
    public void hex() {
        //Math.pow(16,i) 次方
    }


    //public int[] reversePrint(ListNode head) {
        //int[] nums = new int[][];
    //}

    //
    //    private MockMvc mockMvc;
    //
    //    @Autowired
    //    private TestController testController;
    //
    //    @Before
    //    public void setUp(){
    //        logger.info("------初始化执行");
    //        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    //    }
    //
    //    /**
    //     * @Title: test
    //     * @Author : Hujh
    //     * @Date: 2019/7/16 9:46
    //     * @Description : 测试方法
    //     * @param :
    //     * @Return : void
    //     */
    //    @Test
    //    public void test() throws Exception {
    //        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/test/getByParams")
    //                .accept(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON_UTF8)//请求数据格式
    //                .contentType(MediaType.APPLICATION_JSON)//接受所用数据格式
    //                .param("id","123")
    //                .param("name","小张三");
    //
    //        // 执行请求
    //        ResultActions result = mockMvc.perform(requestBuilder);
    //
    //        //结果解析
    //        result.andExpect(MockMvcResultMatchers.status().isOk()) // 执行状态
    ////                .andExpect(MockMvcResultMatchers.jsonPath("name").value("张三")) //期望值
    //                .andDo(MockMvcResultHandlers.print()) // 打印
    //                .andReturn(); // 返回
    //    }
}