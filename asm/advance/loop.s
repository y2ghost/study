.section .data
output:
.asciz "The value is: %d\n"

.section .text
.globl _start
_start:
    movl $100, %ecx
    movl $0, %eax

loop1:
    add %ecx, %eax
    loop loop1
    pushl %eax
    pushl $output
    call printf
    add $8, %esp
    movl $1, %eax
    movl $0, %ebx
    int $0x80
