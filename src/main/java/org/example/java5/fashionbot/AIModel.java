package org.example.java5.fashionbot;

import java.util.*;

public class AIModel {
    private static final Map<String, String> basicRecommendations = new HashMap<>();
    private static final Map<String, String> styleRecommendations = new HashMap<>();
    private static final Map<String, String> colorRecommendations = new HashMap<>();
    private static final Map<String, String> bodyTypeRecommendations = new HashMap<>();
    private static final Map<String, String> mixAndMatchTips = new HashMap<>();

    static {
        // 🌟 Tư vấn theo loại quần áo
        basicRecommendations.put("áo sơ mi", "Bạn muốn mua áo sơ mi cho nam hay nữ? Có thích màu nào không?");
        basicRecommendations.put("quần jean", "Bạn thích kiểu quần jean slim fit, regular fit hay baggy?");
        basicRecommendations.put("giày", "Bạn thích giày thể thao, giày lười hay giày cao gót?");
        basicRecommendations.put("áo khoác", "Bạn cần áo khoác mùa đông hay áo khoác nhẹ?");
        basicRecommendations.put("váy", "Bạn thích váy ngắn hay váy dài? Có màu sắc yêu thích không?");
        basicRecommendations.put("phụ kiện", "Bạn đang tìm kiếm kính mắt, túi xách hay đồng hồ?");
        basicRecommendations.put("đầm", "Bạn muốn chọn đầm dạ hội, đầm công sở hay đầm dạo phố?");
        basicRecommendations.put("quần tây", "Bạn cần quần tây cho công việc hay dự sự kiện?");
        basicRecommendations.put("áo thun", "Bạn thích áo thun basic, oversized hay có họa tiết?");
    }

    static {
        // 🌟 Tư vấn theo mùa/dịp sử dụng
        styleRecommendations.put("mùa hè", "Bạn nên chọn quần áo thoáng mát như áo thun, váy maxi, hoặc quần short.");
        styleRecommendations.put("mùa đông", "Bạn có thể thử áo khoác dạ, áo len, và quần jeans để giữ ấm.");
        styleRecommendations.put("công sở", "Trang phục công sở thường là áo sơ mi, quần tây, và giày da.");
        styleRecommendations.put("dự tiệc", "Bạn có thể thử đầm dạ hội, vest lịch lãm hoặc sơ mi phối với quần âu.");
        styleRecommendations.put("thể thao", "Quần áo thể thao phù hợp gồm áo thun co giãn, quần jogger, giày sneaker.");
        styleRecommendations.put("dạo phố", "Bạn có thể mix áo croptop với quần jean hoặc chân váy midi.");
        styleRecommendations.put("đi biển", "Trang phục đi biển lý tưởng là bikini, áo croptop, quần short.");
    }

    static {
        // 🌟 Tư vấn theo màu sắc
        colorRecommendations.put("trắng", "Màu trắng dễ phối với mọi trang phục, bạn có thể mix với quần jeans hoặc chân váy.");
        colorRecommendations.put("đen", "Màu đen sang trọng và dễ mặc, rất hợp với phong cách công sở hoặc dạ tiệc.");
        colorRecommendations.put("xanh", "Màu xanh mang lại cảm giác tươi mát, phù hợp cho mùa hè.");
        colorRecommendations.put("đỏ", "Màu đỏ nổi bật, giúp bạn thu hút ánh nhìn trong các buổi tiệc.");
        colorRecommendations.put("hồng", "Màu hồng nữ tính, có thể phối với chân váy hoặc quần jean trắng.");
        colorRecommendations.put("be", "Màu be thanh lịch, dễ phối với các gam màu trung tính.");
    }

    static {
        // 🌟 Tư vấn theo vóc dáng
        bodyTypeRecommendations.put("người gầy", "Bạn nên chọn áo oversized, quần ống rộng để trông đầy đặn hơn.");
        bodyTypeRecommendations.put("người tròn", "Chọn trang phục có họa tiết nhỏ, màu tối để tạo cảm giác thon gọn.");
        bodyTypeRecommendations.put("dáng cao", "Bạn có thể thử áo croptop với quần baggy để tôn dáng.");
        bodyTypeRecommendations.put("dáng thấp", "Nên chọn quần cạp cao và áo ngắn để nhìn cao hơn.");
    }

    static {
        // 🌟 Mẹo mix đồ & xu hướng
        mixAndMatchTips.put("phối đồ", "Bạn có thể thử phối áo blazer với quần jean để có phong cách trẻ trung.");
        mixAndMatchTips.put("xu hướng", "Năm nay, phong cách Y2K và minimalism đang được ưa chuộng.");
        mixAndMatchTips.put("chất liệu", "Vải linen phù hợp với mùa hè vì thoáng mát, vải dạ phù hợp với mùa đông.");
    }

    public static String predict(String question) {
        question = question.toLowerCase();

        // ✅ Kiểm tra loại quần áo
        for (Map.Entry<String, String> entry : basicRecommendations.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // ✅ Kiểm tra theo mùa/dịp sử dụng
        for (Map.Entry<String, String> entry : styleRecommendations.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // ✅ Kiểm tra theo màu sắc
        for (Map.Entry<String, String> entry : colorRecommendations.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // ✅ Kiểm tra theo vóc dáng
        for (Map.Entry<String, String> entry : bodyTypeRecommendations.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // ✅ Kiểm tra mẹo phối đồ & xu hướng
        for (Map.Entry<String, String> entry : mixAndMatchTips.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "Bạn có thể mô tả thêm về nhu cầu của mình không? Ví dụ: mua cho dịp nào, mùa nào, hoặc phong cách nào?";
    }
}
