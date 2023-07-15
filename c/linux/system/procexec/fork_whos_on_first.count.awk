#!/usr/bin/awk -f

BEGIN {
    last = -1;
}

{
    if ($1 != last) {
	cat[$2]++;
	tot++;
    }
    last = $1;
}

# 输入文件一般会很大，定义输出信息表明在工作
NR % 200000 == 0 { 
    print "Num children = ", NR / 2;
    for (k in cat) 
 	printf "%-6s %6d %6.2f%%\n", k, cat[k], cat[k] / tot * 100;
}

END {
    print "All done";
    for (k in cat) 
 	printf "%-6s %6d %6.2f%%\n", k, cat[k], cat[k] / tot * 100;
}
