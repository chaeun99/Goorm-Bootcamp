package com.example.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

// http://localhost:8080
@Controller
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/")
    public String viewSchedule(Model model) {
        List<ScheduleEntry> allSchedules = scheduleService.findAll();

        // 주간 시간표
        Map<String, Map<String, ScheduleService.ScheduleBlock>> weeklyTableData =
                scheduleService.generateWeeklyTable(allSchedules);

        model.addAttribute("weeklyTable", weeklyTableData);
        model.addAttribute("daysOfWeek", scheduleService.getDaysOfWeek());
        model.addAttribute("timeSlots", scheduleService.getTimeSlots());

        // 전체 일정, 통계
        model.addAttribute("allSchedules", allSchedules);
        model.addAttribute("scheduleCount", allSchedules.size());
        model.addAttribute("newEntry", new ScheduleEntry());

        return "schedule";
    }
    // 일정 추가
    @PostMapping("/add")
    public String addSchedule(@ModelAttribute ScheduleEntry newEntry, RedirectAttributes redirectAttributes) {
        boolean success = scheduleService.addSchedule(newEntry);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "✅ 일정이 성공적으로 추가되었습니다: " + newEntry.getTitle());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ 시간 충돌이 발생했습니다: " + newEntry.getTitle() + " 또는 시간 형식이 잘못되었습니다.");
        }

        return "redirect:/";
    }

    // 일정 삭제
    @PostMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        scheduleService.deleteSchedule(id);
        redirectAttributes.addFlashAttribute("successMessage", "🗑 일정이 성공적으로 삭제되었습니다.");
        return "redirect:/";
    }
}
