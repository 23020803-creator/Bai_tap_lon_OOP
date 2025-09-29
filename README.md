# Bài tập lớn OOP - Phát triển game Arkanoid
Bài tập lớn này nhằm mục đích củng cố kiến thức về lập trình hướng đối tượng (OOP) thông qua việc phát triển một phiên bản đơn giản của trò chơi kinh điển Arkanoid. Sinh viên sẽ áp dụng các nguyên tắc cơ bản của OOP như đóng gói, kế thừa, đa hình và trừu tượng hóa để xây dựng một ứng dụng có cấu trúc rõ ràng, dễ bảo trì và mở rộng.

# Mô tả về các đối tượng trong trò chơi
Nếu bạn đã từng chơi Arkanoid, bạn sẽ cảm thấy quen thuộc với những đối tượng này. Chúng được được chia làm hai loại chính là nhóm đối tượng động (Paddle, Ball, PowerUp), nhóm đối tượng tĩnh (Brick, Wall).

  * Nhóm đối tượng động:
    1. Paddle (Thanh đỡ):
      - Thanh đỡ là đối tượng chính mà người chơi điều khiển, nó chỉ có thể dịch chuyển sang trái và sang phải trong giới hạn màn hình người chơi.
      - Nhiệm vụ: Thanh đỡ giữ bóng không rơi khỏi màn hình hiển thị, đồng thời có thể thay đổi góc bật lên của bóng khi va chạm.
      - Có thể được tăng/giảm sức mạnh nhờ các Power-up (mở rộng, thu nhỏ, biến đổi, ...).
    2. Ball (Quả bóng):
      - Ball là đối tượng trung tâm của trò chơi, nó luôn di chuyển khi trò chơi bắt đầu và bật lại khi va chạm với Paddle, Wall.
      - Khi va chạm với Brick, nó sẽ làm khối gạch mà nó chạm vào biến mất hoặc giảm độ bền, đồng thời bị bật lại như khi va chạm với Wall.
      - Khi rơi khỏi màn hình chơi (Paddle không đỡ được bóng) thì người chơi sẽ mất một mạng.
    3. Power-up (Vật phẩm hỗ trợ):
      - Vật phẩm hỗ trợ xuất hiện ngẫu nhiên khi Ball va chạm và làm Brick biến mất, vật phẩm hỗ trợ rơi xuống dưới theo trục dọc và người chơi có thể "bắt" bằng Paddle.
      - Một số loại Power-up phổ biến như:
        + ExpandPaddle: tăng kích thước Paddle.
        + FastBall: tăng tốc độ bóng.
        + ExtraLife: thêm mạng.
        + MultiBall: tạo thêm nhiều bóng cùng lúc.

* Nhóm đối tượng tĩnh:
  1. Wall/Border (Tường): Tường bao quanh màn hình chơi, không cho Ball thoát ra (trừ cạnh dưới - nơi chúng ta thiết lập Paddle để che chắn).
  2. Brick (Khối gạch):
    - Brick được bố trí phía cạnh trên của màn hình chơi, có hình dạng tùy thuộc vào từng màn. Người chơi cần phải phá hủy hết các Brick mới có thể qua màn tiếp theo.
    - Có nhiều loại Brick:
      + NormalBrick: bị phá sau 1 lần va chạm.
      + StrongBrick: cần nhiều lần va chạm mới phá hủy.
      + SpecialBrick: khi phá có thể sinh ra Power-up.
* Ngoài hai nhóm đối tượng chính, chúng ta còn có nhóm đối tượng quản lý:
  1. GameManager hoặc GameEngine:
    - Quản lý toàn bộ trạng thái trò chơi: Paddle, Ball, danh sách Brick, Power-up, điểm số, mạng.
    - Kiểm tra va chạm, xử lý logic thắng/thua. - Quản lý vòng đời game: start, pause, game over.
  2. Renderer (Bộ vẽ):
    - Chịu trách nhiệm hiển thị các đối tượng (Paddle, Ball, Brick, Power-up).
    - Giúp tách biệt logic và giao diện để dễ bảo trì, mở rộng.
  
# Mô tả game play
Trong một màn chơi, người chơi sẽ điều khiển Paddle để giữ cho Ball không rơi xuống khỏi màn hình và phá hủy toàn bộ các Brick. Mục tiêu chính là phá hết Brick để qua màn mới.
  1. Quy tắc chơi:
     - Người chơi điều khiển Paddle di chuyển sang trái/phải để đỡ Ball.
     - Ball liên tục di chuyển và thay đổi hướng khi va chạm với Paddle, Wall hoặc Brick.
     - Nếu Ball rơi ra ngoài màn hình (qua cạnh dưới), người chơi mất một mạng. Khi số mạng bằng 0 → Game Over.
     - Khi tất cả Brick bị phá hủy, người chơi qua màn mới.
  2. Xử lý va chạm:
     - Ball – Wall
       + Khi bóng chạm vào tường trái/phải, vận tốc theo trục x sẽ đảo dấu (vx → -vx), còn vận tốc theo trục y giữ nguyên.
       + Khi bóng chạm vào tường trên hoặc mặt dưới của gạch, vận tốc theo trục y sẽ đảo dấu (vy → -vy), còn trục x giữ nguyên.
       + Nếu Ball đi xuống dưới màn hình: người chơi bị mất 1 mạng
     - Ball – Paddle
       + Nếu bóng rơi đúng giữa paddle → phản xạ gần như thẳng đứng lên trên.
       + Nếu bóng rơi gần mép trái → bóng bật lệch về bên trái, góc nghiêng nhiều.
       + Nếu bóng rơi gần mép phải → bóng bật lệch về bên phải.
     - Ball – Brick
       + Khi Ball chạm Brick, Brick bị phá hủy (hoặc giảm độ bền nếu là StrongBrick) và Ball bật lại theo đúng nguyên lý khi va chạm với Wall.
       + Một số Brick khi bị phá hủy sẽ sinh ra Power-up rơi xuống theo trục dọc.
     - Paddle – Power-up
       + Nếu Paddle hứng được Power-up: hiệu ứng sẽ được kích hoạt (mở rộng Paddle, tăng tốc bóng, thêm mạng, tạo nhiều bóng…).
       + Nếu Power-up rơi ra ngoài màn hình → mất hiệu lực.
  3. Xử lý đặc biệt:
     - Multi-ball: Khi hiệu ứng Power-up này được kích hoạt, có nhiều Ball cùng tồn tại trên màn hình. Người chơi phải giữ được ít nhất một Ball còn lại để tiếp tục chơi.
     - Extra life: Giúp người chơi có thêm mạng (tích lũy tối đa 5 mạng), để đề phòng khi chẳng may để Ball rơi.
     - Strong Brick: Cần nhiều lần va chạm mới phá hủy hoàn toàn.
  4. Điều kiện thắng/thua:
     - Thắng: phá hủy toàn bộ Brick trong màn.
     - Thua: tất cả mạng (lives) = 0 khi Ball rơi khỏi màn hình.

# Yêu cầu chung
Trong quá trình thực hiện, sinh viên được phép bổ sung thêm các lớp và hàm cần thiết, cũng như tùy chỉnh cấu trúc chương trình. Có thể sử dụng thêm các thư viện Java bên ngoài nếu cần. Khuyến khích sinh viên sử dụng các công nghệ, thư viện nâng cao để trau dồi kỹ năng.
# Sơ đồ thiết kế các lớp
<img width="3202" height="3384" alt="main" src="https://github.com/user-attachments/assets/5ad06e83-eef5-4d69-92f9-c34c2578858a" />

