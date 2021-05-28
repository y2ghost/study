package ywork.script
// 演示Groovy如何处理 定义和处理 Collections 和 Arrays类型
// List接口使用默认实现类: java.util.ArrayList
List<Integer> list1 = [1, 2, 3, 4]
println list1
println list1.size()
println '---------------'

def list2 = [1, 2, 3, 4]
println list2
println list2.size()
println list2.getClass()
println '---------------'

// as关键字是强制类型转换，编译器自动转换类型
// 而非casting这种运行时的类型转换
def list3 = [1, 2, 3, 4] as LinkedList
println list3
println list3.getClass()
println '---------------'

// 可以使用数组索引
def aSet = [1, 2, 3, 4]
println aSet
println aSet.getClass()
println aSet[0]
println aSet[1]
println aSet[aSet.size() -1]
aSet[0] = 5
println aSet
println '---------------'

// 使用明确的类型
TreeSet aTreeSet = [3, 2, 4, 1]
println aTreeSet
println aTreeSet.getClass()
println '---------------'

// 使用<<追加值
def fruits = ["apple", "banana"]
fruits << "orange"
println fruits
println '---------------'

// 列表的列表
def lists = [[1, 2], [10, 20]]
println lists
println '---------------'

// 数组不允许如下的定义方式
// int[] num = {1,2,3,4}
// 原因{}用于闭包的语法
int[] num = [10, 2, 3, 46]
print num;
println '---------------'

def arr = [1, 2, 3] as int[];
println arr
println arr.getClass().isArray()
println arr.getClass().getComponentType()
println '---------------'

def multiArrays = [[1, 2], [10, 20]] as long[][]
println multiArrays
println multiArrays.getClass().isArray()
println multiArrays.getClass().getComponentType()
println multiArrays.getClass().getComponentType().getComponentType()
println '---------------'

// Map类型，默认使用类型: LinkedHashMap
def nums = [one: 1, two: 2, three: 3]
println nums.getClass()
println nums

// 指定类型
def TreeMap nums2 = [1: 'one', 2: 'two', 3: 'three']
println nums2.getClass()
println nums2

def food = [breakfast: "egg", lunch: "sushi", dinner: "pasta" ]
println food.getClass()
println food
println '---------------'


// 使用[]操作符读写Map对象
HashMap shnums = [one: 1, two: 2, three: 3]
println shnums['one']
shnums['four'] = 4
println shnums
println '---------------'

// 使用变量作为键值
def key1= 'one'
def value2= 2;
def varnums1 = [(key1): 1, two: value2]
println varnums1

def key= 1
def str= "wo"
def varnums2 = [(key): "one", (key+1): "t${str}"]
println varnums2
