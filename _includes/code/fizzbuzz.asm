section .data
  f db "fizz    "
  b db "buzz    "
  fb db "fizzbuzz"
  n db 10 ; newline string
  cycle dq num, num, f, num, b, f, num, num, f, b, num, f, num, num, fb
  callid dq 13, 1 ; sys_time, sys_exit

section .bss
  num resb 8 ; number buffer

section .text
global _start

print:      ;write rcx
  mov rax,4 ;sys_write
  mov rbx,1 ;stdout
  mov rdx,8
  int 0x80
  ret

newline:    ;write newline
  mov rax,4 ;sys_write
  mov rbx,1 ;stdout
  mov rcx,n
  mov rdx,1
  int 0x80
  ret

itoa:      ;convert rax to str
  mov byte[num+1],0x30
  mov rdx,0
  mov rcx,10
  div rcx
  add [num+1],rdx
  mov byte[num],0x30
  mov rdx,0
  mov rcx,10
  div rcx
  add [num],rdx
  ret

_start:
  mov r12,0

hundredtimes:
  ; initialise number buffer
  mov rax,r12
  inc rax
  call itoa

  ; mod 15 the number
  mov rax,r12
  mov rdx,0
  mov rcx,15
  div rcx

  ; look up the number in cycle
  ; prints the num buffer or any of the strings
  mov rcx,[cycle+rdx*8]
  call print
  call newline
  
  ; next...
  inc r12

  ; devide the number by 100
  mov rax,r12
  mov rdx,0
  mov rcx,100
  div rcx

  ; get the time or exit
  mov rax,[callid+rax*8]
  int 0x80

  ; jump to the top of the loop
  jmp hundredtimes

