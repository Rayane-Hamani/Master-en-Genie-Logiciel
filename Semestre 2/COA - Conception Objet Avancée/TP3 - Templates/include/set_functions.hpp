#ifndef __SET_FUNCTIONS__
#define __SET_FUNCTIONS__

#include <vector>

using namespace std;

inline void set_intersection_nt(std::vector<int>::const_iterator a_begin, 
                         std::vector<int>::const_iterator a_end, 
                         std::vector<int>::const_iterator b_begin, 
                         std::vector<int>::const_iterator b_end,
                         std::back_insert_iterator<std::vector<int>> c_begin)
{
    for(auto i = a_begin; i < a_end; i++){
        if(find(b_begin,b_end,*i) != b_end) c_begin = *i;
    }
}


inline void set_union_nt(std::vector<int>::const_iterator a_begin, 
                  std::vector<int>::const_iterator a_end, 
                  std::vector<int>::const_iterator b_begin, 
                  std::vector<int>::const_iterator b_end,
                  std::back_insert_iterator<std::vector<int>> c_begin)
{
    for(auto i = a_begin; i < a_end; i++) c_begin = *i;
    for(auto i2 = b_begin; i2 < b_end; i2++){
        if(find(a_begin,a_end,*i2) == a_end) c_begin = *i2;
    }
}

// Écrire les fonctions set_intersect_t() et set_union_t() ici
template<class C, class C2>
inline void set_intersection_t(C a_begin, C a_end, C b_begin, C b_end, C2 c_begin){
    for(C i = a_begin; i != a_end; i++){
        if(find(b_begin, b_end, *i) != b_end) c_begin = *i;
    }
}

template<class C, class C2>
inline void set_union_t(C a_begin, C a_end, C b_begin, C b_end, C2 c_begin){
    for(C i = a_begin; i != a_end; i++) c_begin = *i;
    for(C i2 = b_begin; i2 != b_end; i2++){
        if(find(a_begin, a_end, *i2) == a_end) c_begin = *i2;
    }
}


template<class C, class C2, class C3, class C4>
inline void set_intersection_t(C a_begin, C a_end, C2 b_begin, C2 b_end, C3 c_begin, C4 compare){
    for(C i = a_begin; i != a_end; i++){
        for(C2 i2 = b_begin; i2 != b_end; i2++){    
            if(compare(*i,*i2)) c_begin = *i;
        }
    }
}

inline bool compare(const std::pair<int,string> &x, int y){
    return x.first == y;
}


#endif
