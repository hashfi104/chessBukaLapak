Buat aplikasi untuk menampilkan sebuah papan catur beserta bidaknya.

Posisi bidak diambil lewat tcp socket di

`server: xinuc.org`

`port: 7387`

Gunakan koneksi socket persistent karena server akan mengirimkan dan akan melakukan update posisi bidak setiap beberapa detik.

Aplikasi Anda harus menyesuaikan posisi bidak dengan posisi baru yang dikirim oleh server.

Posisi bidak mengikuti standar papan catur seperti pada gambar [chess_board2](https://gist.github.com/hasanizer/fac292a550fb55a19964#file-chess_board2-png)

Posisi akan dikirimkan dengan format:
`<kode bidak><posisi horizontal><posisi vertikal>[spasi]<kode bidak><posisi horizontal><posisi vertikal>` dst

Kode Bidak:
```
K: White King
Q: White Queen
B: White Bishop
N: White Knight
R: White Rook
k: Black King
q: Black Queen
b: Black Bishop
n: Black Knight
r: Black Rook
```

Contoh tampilan papan catur untuk posisi
*Ka1 Qg3 Be6 Ne4 Rd1 kg6 qa4 bf5 ng1 rf3*
[example]([chess_board](https://gist.github.com/xinuc/fc3bb9f5f054522ee9c8#file-chess_board-png))